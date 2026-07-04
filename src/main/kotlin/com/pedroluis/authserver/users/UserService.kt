package com.pedroluis.authserver.users

import com.pedroluis.authserver.exceptions.BadRequestException
import com.pedroluis.authserver.exceptions.NotFoundException
import com.pedroluis.authserver.exceptions.UnauthorizedException
import com.pedroluis.authserver.files.S3Storage
import com.pedroluis.authserver.roles.RoleRepository
import com.pedroluis.authserver.security.Jwt
import com.pedroluis.authserver.users.responses.LoginResponse
import com.pedroluis.authserver.users.responses.UserResponse
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import java.io.ByteArrayInputStream
import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.security.MessageDigest
import java.util.Locale

@Service
class UserService(
    val repository: UserRepository,
    val roleRepository: RoleRepository,
    val jwt: Jwt,
    private val s3Storage: S3Storage,
    val webClientBuilder: WebClient.Builder
) {
    private val webClient = webClientBuilder.build()

    fun insert(user: User): User {
        if (repository.findByEmail(user.email) != null) {
            throw BadRequestException("User already exists")
        }
        val savedUser = repository.save(user)
        resolveAndSaveAvatar(savedUser)
        return savedUser
    }

    fun findAll(dir: SortDir = SortDir.ASC) = when (dir) {
        SortDir.ASC -> repository.findAll(Sort.by("name").ascending())
        SortDir.DESC -> repository.findAll(Sort.by("name").descending())
    }

    fun findByIdOrNull(id: Long) = repository.findByIdOrNull(id)
    fun findById(id: Long) = repository.findByIdOrNull(id) ?: throw NotFoundException(id)

    fun delete(id: Long) {
        val user = findById(id)
        if (user.isAdmin() && repository.findByRole("ADMIN").size == 1) {
            throw BadRequestException("Cannot delete the last admin")
        }
        repository.delete(user)
        log.info("User $id deleted successfully")
    }

    fun findByRole(role: String) = repository.findByRole(role)

    fun addRole(id: Long, roleName: String): Boolean {
        val upperRole = roleName.uppercase()
        val user = findById(id)
        if (user.roles.any { it.name == upperRole }) return false

        val role = roleRepository.findByName(upperRole) ?: throw BadRequestException("Role $upperRole not found")

        user.roles.add(role)
        repository.save(user)
        log.info("User $id successfully added to role $role")
        return true
    }

    fun update(id: Long, name: String): User? {
        val user = findById(id)
        if (user.name == name) {
            return null
        }
        user.name = name
        repository.save(user)
        return user
    }

    fun login(email: String, password: String): LoginResponse {
        val user = repository.findByEmail(email) ?: throw UnauthorizedException("User $email not found")

        if (user.password != password)
            throw UnauthorizedException("Invalid password")

        log.info("User ${user.id} is logged in")
        return LoginResponse(
            token = jwt.createToken(user),
            UserResponse(user)
        )
    }

    fun resetAvatar(userId: Long): User {
        val user = findById(userId)
        resolveAndSaveAvatar(user)
        return user
    }

    private fun resolveAndSaveAvatar(user: User) {
        val gravatarHash = md5(user.email.lowercase(Locale.getDefault()).trim())
        val gravatarUrl = "https://www.gravatar.com/avatar/$gravatarHash?d=404"

        var avatarBytes: ByteArray? = null

        try {
            avatarBytes = webClient.get().uri(gravatarUrl)
                .retrieve()
                .bodyToMono(ByteArray::class.java)
                .block()
            log.info("Gravatar image fetched for user ${user.id}")
        } catch (e: WebClientResponseException.NotFound) {
            log.info("Gravatar not found for user ${user.id}, falling back to UI-Avatars.")
        } catch (e: Exception) {
            log.error("Error fetching Gravatar for user ${user.id}: ${e.message}", e)
        }

        if (avatarBytes == null || avatarBytes.isEmpty()) {
            val uiAvatarsName = user.name.ifBlank { user.email.substringBefore("@") }
            val uiAvatarsUrl =
                "https://ui-avatars.com/api/?name=${uiAvatarsName}&background=random&color=fff&format=png"
            try {
                avatarBytes = webClient.get().uri(uiAvatarsUrl)
                    .retrieve()
                    .bodyToMono(ByteArray::class.java)
                    .block()
                log.info("UI-Avatars image fetched for user ${user.id}")
            } catch (e: Exception) {
                log.error("Error fetching UI-Avatars for user ${user.id}: ${e.message}", e)
            }
        }

        avatarBytes?.let {
            val s3Path = "avatars/${user.id}.png"
            val multipartFile = ByteArrayMultipartFile(it, "avatar.png", "image/png")
            s3Storage.save(user, s3Path, multipartFile)
            user.avatarUrl = s3Storage.urlFor(s3Path)
            repository.save(user)
            log.info("Avatar saved to S3 for user ${user.id}: ${user.avatarUrl}")
        } ?: run {
            log.warn("No avatar could be generated or saved for user ${user.id}")
        }
    }

    private fun md5(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        val digest = md.digest(input.toByteArray())
        return digest.joinToString("") { "%02x".format(it) }
    }

    class ByteArrayMultipartFile(
        private val byteArray: ByteArray,
        private val filename: String,
        private val contentType: String
    ) : MultipartFile {
        override fun getName(): String = "file"
        override fun getOriginalFilename(): String = filename
        override fun getContentType(): String = contentType
        override fun isEmpty(): Boolean = byteArray.isEmpty()
        override fun getSize(): Long = byteArray.size.toLong()
        override fun getBytes(): ByteArray = byteArray
        override fun getInputStream(): InputStream = ByteArrayInputStream(byteArray)
        override fun transferTo(dest: File) {
            Files.copy(inputStream, dest.toPath(), StandardCopyOption.REPLACE_EXISTING)
        }

        override fun transferTo(dest: Path) {
            Files.copy(inputStream, dest, StandardCopyOption.REPLACE_EXISTING)
        }
    }

    companion object {
        val log = LoggerFactory.getLogger(UserService::class.java)
    }
}