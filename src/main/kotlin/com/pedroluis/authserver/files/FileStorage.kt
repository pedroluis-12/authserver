package com.pedroluis.authserver.files

import com.pedroluis.authserver.users.User
import org.springframework.core.io.Resource
import org.springframework.web.multipart.MultipartFile

interface FileStorage {
    fun save(user: User, path: String, file: MultipartFile)
    fun load(path: String): Resource?
    fun urlFor(name: String): String
}