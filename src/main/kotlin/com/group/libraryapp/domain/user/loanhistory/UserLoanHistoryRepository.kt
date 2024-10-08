package com.group.libraryapp.domain.user.loanhistory

import com.group.libraryapp.dto.user.response.UserLoanHistoryResponse
import org.springframework.data.jpa.repository.JpaRepository

interface UserLoanHistoryRepository: JpaRepository<UserLoanHistory, Long>