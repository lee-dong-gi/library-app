package com.group.libraryapp.service.book

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.book.BookRepository
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
import com.group.libraryapp.dto.book.request.BookLoanRequest
import com.group.libraryapp.dto.book.request.BookRequest
import com.group.libraryapp.dto.book.request.BookReturnRequest
import com.group.libraryapp.utill.fail
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BookService (
    private val bookRepository: BookRepository,
    private val userRepository: UserRepository,
    private val userLoanHistoryRepository: UserLoanHistoryRepository,
) {
    @Transactional
    fun saveBook(req: BookRequest) {
        val book = Book(req.name, req.type)
        bookRepository.save(book)
    }

    @Transactional
    fun loanBook(req: BookLoanRequest) {
        val book = bookRepository.findByName(req.bookName) ?: fail()
        if (userLoanHistoryRepository.findByBookNameAndStatus(req.bookName, UserLoanStatus.LOANED) != null) {
            throw IllegalArgumentException("진작 대출되어 있는 책입니다")
        }

        val user = userRepository.findByName(req.userName) ?: fail()
        user.loanBook(book)
    }
    @Transactional
    fun returnBook(req: BookReturnRequest) {
        val user = userRepository.findByName(req.userName) ?: fail()
        user.returnBook(req.bookName)
    }
}