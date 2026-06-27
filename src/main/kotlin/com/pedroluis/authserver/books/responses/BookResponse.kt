package com.pedroluis.authserver.books.responses

import com.pedroluis.authserver.books.Book

data class BookResponse(
    val id: Long?,
    val title: String,
    val author: String,
    val available: Boolean
) {
    constructor(book: Book) : this(
        id = book.id,
        title = book.title,
        author = book.author,
        available = book.available
    )
}