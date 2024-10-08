package com.group.libraryapp.utill

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class TxHelper {

    // 어떠한 함수를 전달받아 그 함수를 실행시켜주는 역할
    @Transactional
    fun exec(block: () -> Unit) {
        block()
    }
}