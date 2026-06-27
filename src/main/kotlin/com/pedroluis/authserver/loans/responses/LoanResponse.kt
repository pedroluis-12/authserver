package com.pedroluis.authserver.loans.responses

import com.pedroluis.authserver.loans.Loan
import java.time.LocalDate

data class LoanResponse(
    val id: Long?,
    val bookId: Long?,
    val bookTitle: String,
    val userId: Long?, // Adicionando o ID do usuário
    val userName: String, // Adicionando o nome do usuário
    val loanDate: LocalDate,
    val returnDate: LocalDate?
) {
    constructor(loan: Loan) : this(
        id = loan.id,
        bookId = loan.book.id,
        bookTitle = loan.book.title,
        userId = loan.user.id, // Obtendo o ID do usuário da entidade Loan
        userName = loan.user.name, // Obtendo o nome do usuário da entidade Loan
        loanDate = loan.loanDate,
        returnDate = loan.returnDate
    )
}