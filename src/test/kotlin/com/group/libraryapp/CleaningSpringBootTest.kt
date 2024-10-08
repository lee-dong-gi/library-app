package com.group.libraryapp

import org.junit.jupiter.api.AfterEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.jpa.repository.JpaRepository

@SpringBootTest
class CleaningSpringBootTest {

    @Autowired
    private lateinit var repositories: List<JpaRepository<*, *>>

    @AfterEach
    fun clean() {
        val currentMillis = System.currentTimeMillis()
        repositories.forEach { it.deleteAll() }
        println("소요 시간 : ${System.currentTimeMillis() - currentMillis}")
    }

}