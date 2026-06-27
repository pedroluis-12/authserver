package com.pedroluis.authserver.books

import com.pedroluis.authserver.books.requests.CreateBookRequest
import com.pedroluis.authserver.books.requests.UpdateBookRequest
import com.pedroluis.authserver.exceptions.NotFoundException
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service

@Service
class BookService(
    private val repository: BookRepository
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun insert(data: CreateBookRequest): Book {
        log.info("Inserting book: {}", data.title)
        val book = data.toBook()
        return repository.save(book)
    }

    fun findById(id: Long): Book =
        repository.findById(id).orElseThrow { NotFoundException("Book $id not found!") }

    fun findAll(
        title: String?,
        author: String?,
        available: Boolean?,
        pageable: Pageable
    ): Page<Book> {
        log.info("Searching for books with title: {}, author: {}, available: {}", title, author, available)
        val specification = Specification<Book> { root, query, criteriaBuilder ->
            val predicates = mutableListOf(criteriaBuilder.conjunction())

            title?.let {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%${it.lowercase()}%"))
            }
            author?.let {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("author")), "%${it.lowercase()}%"))
            }
            available?.let {
                predicates.add(criteriaBuilder.equal(root.get<Boolean>("available"), it))
            }
            criteriaBuilder.and(*predicates.toTypedArray())
        }
        return repository.findAll(specification, pageable)
    }

    fun update(id: Long, data: UpdateBookRequest): Book {
        log.info("Updating book {}: {}", id, data.title)
        val book = repository.findById(id).orElseThrow { NotFoundException("Book $id not found!") }

        data.title?.let { book.title = it }
        data.author?.let { book.author = it }
        data.available?.let { book.available = it }

        return repository.save(book)
    }

    fun delete(id: Long) {
        log.info("Deleting book {}", id)
        if (!repository.existsById(id)) {
            throw NotFoundException("Book $id not found!")
        }
        repository.deleteById(id)
    }
}