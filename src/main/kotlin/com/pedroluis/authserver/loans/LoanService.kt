package com.pedroluis.authserver.loans

import com.pedroluis.authserver.books.BookRepository
import com.pedroluis.authserver.exceptions.NotFoundException
import com.pedroluis.authserver.exceptions.BadRequestException
import com.pedroluis.authserver.loans.requests.CreateLoanRequest
import com.pedroluis.authserver.loans.requests.UpdateLoanRequest
import com.pedroluis.authserver.users.UserRepository // Importar UserRepository
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class LoanService(
    private val loanRepository: LoanRepository,
    private val bookRepository: BookRepository,
    private val userRepository: UserRepository // Injetar UserRepository
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun insert(data: CreateLoanRequest): Loan {
        log.info("Creating loan for book ID: {} and user ID: {}", data.bookId, data.userId)
        val book = bookRepository.findById(data.bookId!!)
            .orElseThrow { NotFoundException("Book ${data.bookId} not found!") }

        val user = userRepository.findById(data.userId!!) // Buscar o usuário
            .orElseThrow { NotFoundException("User ${data.userId} not found!") }

        if (!book.available) {
            throw BadRequestException("Book ${book.title} is not available for loan.")
        }

        book.available = false
        bookRepository.save(book)

        val loan = Loan(
            book = book,
            user = user, // Associar o usuário ao empréstimo
            loanDate = data.loanDate ?: LocalDate.now()
        )
        return loanRepository.save(loan)
    }

    fun findById(id: Long): Loan =
        loanRepository.findById(id).orElseThrow { NotFoundException("Loan $id not found!") }

    fun findAll(
        bookTitle: String?,
        userName: String?, // Adicionar filtro por nome de usuário
        loanDate: LocalDate?,
        returnDate: LocalDate?,
        pageable: Pageable
    ): Page<Loan> {
        log.info("Searching for loans with bookTitle: {}, userName: {}, loanDate: {}, returnDate: {}", bookTitle, userName, loanDate, returnDate)
        val specification = Specification<Loan> { root, query, criteriaBuilder ->
            val predicates = mutableListOf(criteriaBuilder.conjunction())

            bookTitle?.let {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get<String>("book").get("title")), "%${it.lowercase()}%"))
            }
            userName?.let { // Adicionar predicado para nome de usuário
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get<String>("user").get("name")), "%${it.lowercase()}%"))
            }
            loanDate?.let {
                predicates.add(criteriaBuilder.equal(root.get<LocalDate>("loanDate"), it))
            }
            returnDate?.let {
                predicates.add(criteriaBuilder.equal(root.get<LocalDate>("returnDate"), it))
            }
            criteriaBuilder.and(*predicates.toTypedArray())
        }
        return loanRepository.findAll(specification, pageable)
    }

    fun update(id: Long, data: UpdateLoanRequest): Loan {
        log.info("Updating loan {}", id)
        val loan = loanRepository.findById(id).orElseThrow { NotFoundException("Loan $id not found!") }

        data.loanDate?.let { loan.loanDate = it }
        data.returnDate?.let {
            if (it.isBefore(loan.loanDate)) {
                throw BadRequestException("Return date cannot be before loan date.")
            }
            loan.returnDate = it
            loan.book.available = true // Mark book as available upon return
            bookRepository.save(loan.book)
        }

        return loanRepository.save(loan)
    }

    fun delete(id: Long) {
        log.info("Deleting loan {}", id)
        val loan = loanRepository.findById(id).orElseThrow { NotFoundException("Loan $id not found!") }

        if (loan.returnDate == null) {
            throw BadRequestException("Cannot delete an active loan. Please return the book first.")
        }

        loanRepository.delete(loan)
    }
}