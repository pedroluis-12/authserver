package com.pedroluis.authserver.loans

import com.pedroluis.authserver.books.Book
import com.pedroluis.authserver.users.User // Importar a entidade User
import jakarta.persistence.*
import java.time.LocalDate

@Entity
class Loan(
    @Id @GeneratedValue
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    var book: Book,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @Column(nullable = false)
    var loanDate: LocalDate = LocalDate.now(),

    var returnDate: LocalDate? = null
)