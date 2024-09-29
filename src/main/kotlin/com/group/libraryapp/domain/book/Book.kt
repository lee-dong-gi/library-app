package com.group.libraryapp.domain.book

import javax.persistence.*

@Entity
class Book (
    val name: String,

    @Enumerated(EnumType.STRING)
    val type: BookType,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
){

    init {
        if (name.isBlank()) {
            throw IllegalArgumentException("이름은 비어 있을 수 없습니다")
        }
    }

    // object mother 패턴, kotlin 컨벤션 상으로 companion object는 가장 밑에 작성
    companion object {
        fun fixture (
            name: String = "책 이름",
            type: BookType = BookType.COMPUTER,
            id: Long? = null,
        ): Book {
            return Book(
                name = name,
                type = type,
                id = id
            )
        }
    }

}