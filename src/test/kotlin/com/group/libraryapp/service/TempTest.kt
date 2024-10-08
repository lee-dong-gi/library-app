package com.group.libraryapp.service

import com.group.libraryapp.CleaningSpringBootTest
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.service.user.UserService
import com.group.libraryapp.utill.TxHelper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
class TempTest @Autowired constructor(
    private val userService: UserService,
    private val userRepository: UserRepository,
    private val userLoanHistoryRepository: UserLoanHistoryRepository,
    private val txHelper: TxHelper,
): CleaningSpringBootTest() {

    /**
     * - 문제점 -
     *  failed to lazily initialize a collection of role: com.group.libraryapp.domain.user.User.userLoanHistories, could not initialize proxy - no Session
     *  @Transactional 없으면 영속성 컨텍스트가 존재하지 않아 위 에러 발생
     *  @Transactional 장점 : 롤백된다, 트랜잭션별로 테스트가 격리되어 병렬테스트 가능
     *  @Transactional 단점 : 테스트 내성이 떨어진다.(만약 실제 로직에 @Transactional이 없는 경우 다르게 동작함)
     *  롤백의 경우 해결책으로 CleaningSpringBootTest 클래스를 만들어서 참조받으면 되긴함
     */
    @Test
    fun `해결책 1, 유저 1명과 책 2권을 저장하고 대출한다 - @Transactional 미사용(N쪽 Repo 이용)`() {
        // when
        userService.saveUserAndLoanTwoBooks()

        // then
        val users = userRepository.findAll()
        assertThat(users).hasSize(1)

        val histories = userLoanHistoryRepository.findAll()
        assertThat(histories).hasSize(2)
        assertThat(histories[0].user.id).isEqualTo(users[0].id)
    }

    @Test
    fun `해결책 2, 유저 1명과 책 2권을 저장하고 대출한다 - @Transactional 미사용(fetch join 이용)`() {
        // when
        userService.saveUserAndLoanTwoBooks()

        // then
        val users = userRepository.findAllWithHistories() // 애초에 fetch join으로 userLoanHistories를 가져오므로 @Transactional 없이 가능
        assertThat(users).hasSize(1)

        // @Transaction 사용 시 아래코드 활성화
        assertThat(users[0].userLoanHistories).hasSize(2)
    }

    @Transactional
    @Test
    fun `해결책 3, 유저 1명과 책 2권을 저장하고 대출한다 - @Transactional 사용`() {
        // when
        userService.saveUserAndLoanTwoBooks()

        // then
        val users = userRepository.findAll()
        assertThat(users).hasSize(1)
        assertThat(users[0].userLoanHistories).hasSize(2)
    }

    @Test
    fun `해결책 4, 유저 1명과 책 2권을 저장하고 대출한다 - TxHelper 사용`() {
        // when
        userService.saveUserAndLoanTwoBooks()

        // then
        txHelper.exec {
            val users = userRepository.findAll()
            assertThat(users).hasSize(1)

            assertThat(users[0].userLoanHistories).hasSize(2)
        }
    }
}