package com.pedroluis.authserver.loans.requests

import jakarta.validation.constraints.NotNull
import java.time.LocalDate

data class CreateLoanRequest(
    @NotNull
    val bookId: Long?,

    @NotNull // Adicionando o userId
    val userId: Long?,

    val loanDate: LocalDate? = LocalDate.now()
)