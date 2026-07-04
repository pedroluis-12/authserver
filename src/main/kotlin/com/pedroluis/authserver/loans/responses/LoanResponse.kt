package com.pedroluis.authserver.loans.responses

import com.pedroluis.authserver.loans.Loan
import java.time.LocalDate

data class LoanResponse(
    val id: Long?,
    val bookId: Long?,
    val bookTitle: String,
    val userId: Long?,
    val userName: String,
    val loanDate: LocalDate,
    val returnDate: LocalDate?
) {
    constructor(loan: Loan) : this(
        id = loan.id,
        bookId = loan.book.id,
        bookTitle = loan.book.title,
        userId = loan.user.id,
        userName = loan.user.name,
        loanDate = loan.loanDate,
        returnDate = loan.returnDate
    )
}