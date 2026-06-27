package com.pedroluis.authserver.books

import com.pedroluis.authserver.books.requests.CreateBookRequest
import com.pedroluis.authserver.books.requests.UpdateBookRequest
import com.pedroluis.authserver.books.responses.BookResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/books")
@SecurityRequirement(name = "jwt-auth")
class BookController(
    private val service: BookService
) {
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    fun create(@RequestBody @Valid request: CreateBookRequest): ResponseEntity<BookResponse> =
        service.insert(request)
            .let { BookResponse(it) }
            .let { ResponseEntity.status(HttpStatus.CREATED).body(it) }

    @GetMapping("{id}")
    @PreAuthorize("isAuthenticated()")
    fun getById(@PathVariable id: Long): ResponseEntity<BookResponse> =
        service.findById(id)
            .let { BookResponse(it) }
            .let { ResponseEntity.ok(it) }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    fun getAll(
        @RequestParam(required = false) title: String?,
        @RequestParam(required = false) author: String?,
        @RequestParam(required = false) available: Boolean?,
        @PageableDefault(size = 10, sort = ["title"]) pageable: Pageable
    ): ResponseEntity<List<BookResponse>> =
        service.findAll(title, author, available, pageable)
            .map { BookResponse(it) }
            .let { ResponseEntity.ok(it.content) }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun update(
        @PathVariable id: Long,
        @RequestBody @Valid request: UpdateBookRequest
    ): ResponseEntity<BookResponse> =
        service.update(id, request)
            .let { BookResponse(it) }
            .let { ResponseEntity.ok(it) }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        service.delete(id)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}