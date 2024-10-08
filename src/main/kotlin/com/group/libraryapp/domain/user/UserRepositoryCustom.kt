package com.group.libraryapp.domain.user

interface UserRepositoryCustom {

    /**
     *  User -< UserLoanHistory
     *  User -< UserPrivacy
     *  --> fetch join은 1개의 N에 대해서만 사용 할 수 있다.
     */
    fun findAllWithHistories(): List<User>

}