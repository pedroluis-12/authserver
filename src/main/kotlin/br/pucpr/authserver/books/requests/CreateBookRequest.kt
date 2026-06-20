package br.pucpr.authserver.books.requests

import br.pucpr.authserver.books.Book
import jakarta.validation.constraints.NotBlank

data class CreateBookRequest(
    @NotBlank
    val title: String?,

    @NotBlank
    val author: String?
) {
    fun toBook() = Book(
        title = title!!,
        author = author!!
    )
}