package br.pucpr.authserver.books

import br.pucpr.authserver.loans.Loan
import jakarta.persistence.*

@Entity
class Book(
    @Id @GeneratedValue
    var id: Long? = null,

    @Column(nullable = false)
    var title: String,

    @Column(nullable = false)
    var author: String,

    @Column(nullable = false)
    var available: Boolean = true,

    @OneToMany(mappedBy = "book", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var loans: MutableList<Loan> = mutableListOf()
)