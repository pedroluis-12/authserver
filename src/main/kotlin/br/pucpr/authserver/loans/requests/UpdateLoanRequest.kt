package br.pucpr.authserver.loans.requests

import java.time.LocalDate

data class UpdateLoanRequest(
    val loanDate: LocalDate?,
    val returnDate: LocalDate?
)