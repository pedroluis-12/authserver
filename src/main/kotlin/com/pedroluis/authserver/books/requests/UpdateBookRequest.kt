package com.pedroluis.authserver.books.requests

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UpdateBookRequest(
    @NotBlank @Size(min = 1, max = 50)
    val title: String?,

    @NotBlank @Size(min = 1, max = 50)
    val author: String?,

    val available: Boolean?
)