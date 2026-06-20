package br.pucpr.authserver.loans.responses

import br.pucpr.authserver.loans.Loan
import java.time.LocalDate

data class LoanResponse(
    val id: Long?,
    val bookId: Long?,
    val bookTitle: String,
    val loanDate: LocalDate,
    val returnDate: LocalDate?
) {
    constructor(loan: Loan) : this(
        id = loan.id,
        bookId = loan.book.id,
        bookTitle = loan.book.title,
        loanDate = loan.loanDate,
        returnDate = loan.returnDate
    )
}