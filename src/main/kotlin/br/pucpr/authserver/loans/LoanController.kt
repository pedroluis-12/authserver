package br.pucpr.authserver.loans

import br.pucpr.authserver.loans.requests.CreateLoanRequest
import br.pucpr.authserver.loans.requests.UpdateLoanRequest
import br.pucpr.authserver.loans.responses.LoanResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/loans")
@SecurityRequirement(name = "jwt-auth")
class LoanController(
    private val service: LoanService
) {
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    fun create(@RequestBody @Valid request: CreateLoanRequest): ResponseEntity<LoanResponse> =
        service.insert(request)
            .let { LoanResponse(it) }
            .let { ResponseEntity.status(HttpStatus.CREATED).body(it) }

    @GetMapping("{id}")
    @PreAuthorize("isAuthenticated()")
    fun getById(@PathVariable id: Long): ResponseEntity<LoanResponse> =
        service.findById(id)
            .let { LoanResponse(it) }
            .let { ResponseEntity.ok(it) }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    fun getAll(
        @RequestParam(required = false) bookTitle: String?,
        @RequestParam(required = false) loanDate: LocalDate?,
        @RequestParam(required = false) returnDate: LocalDate?,
        @PageableDefault(size = 10, sort = ["loanDate"]) pageable: Pageable
    ): ResponseEntity<List<LoanResponse>> =
        service.findAll(bookTitle, loanDate, returnDate, pageable)
            .map { LoanResponse(it) }
            .let { ResponseEntity.ok(it.content) }

    @PutMapping("{id}")
    @PreAuthorize("isAuthenticated()")
    fun update(
        @PathVariable id: Long,
        @RequestBody @Valid request: UpdateLoanRequest
    ): ResponseEntity<LoanResponse> =
        service.update(id, request)
            .let { LoanResponse(it) }
            .let { ResponseEntity.ok(it) }

    @DeleteMapping("{id}")
    @PreAuthorize("isAuthenticated()")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        service.delete(id)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}