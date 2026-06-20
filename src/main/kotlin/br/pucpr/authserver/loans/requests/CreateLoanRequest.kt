package br.pucpr.authserver.loans.requests

import jakarta.validation.constraints.NotNull
import java.time.LocalDate

data class CreateLoanRequest(
    @NotNull
    val bookId: Long?,

    val loanDate: LocalDate? = LocalDate.now()
)