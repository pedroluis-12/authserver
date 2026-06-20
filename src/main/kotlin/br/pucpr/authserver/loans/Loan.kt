package br.pucpr.authserver.loans

import br.pucpr.authserver.books.Book
import jakarta.persistence.*
import java.time.LocalDate

@Entity
class Loan(
    @Id @GeneratedValue
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    var book: Book,

    @Column(nullable = false)
    var loanDate: LocalDate = LocalDate.now(),

    var returnDate: LocalDate? = null
)