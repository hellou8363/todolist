package org.zerock.todolist.domain.todo

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.test.context.ActiveProfiles
import org.zerock.todolist.config.QueryDslConfig
import org.zerock.todolist.domain.todo.dto.CreateTodoRequest
import org.zerock.todolist.domain.todo.model.Todo
import org.zerock.todolist.domain.todo.repository.TodoRepository
import org.zerock.todolist.domain.todo.type.SearchType
import org.zerock.todolist.domain.user.dto.CreateUserRequest
import org.zerock.todolist.domain.user.model.User
import org.zerock.todolist.domain.user.repository.UserRepository

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(value = [QueryDslConfig::class]) // @DataJpaTest에 포함 X, import 필요
@ActiveProfiles("test")
class TodoRepositoryTests @Autowired constructor(
    private val todoRepository: TodoRepository,
    private val userRepository: UserRepository
) {
    init {
        userRepository.save(user) // 모든 테스트에서 공통으로 사용되는 User
    }

    @Test
    fun `SearchType이 NONE일 경우 전체 데이터 조회되는지 확인`() {
        // GIVEN
        todoRepository.saveAllAndFlush(DEFAULT_TODO_LIST)

        // WHEN
        val result1 = todoRepository.search(SearchType.NONE, "", Pageable.ofSize(10))
        val result2 = todoRepository.search(SearchType.NONE, "", Pageable.ofSize(5))
        val result3 = todoRepository.search(SearchType.NONE, "", Pageable.ofSize(15))

        // THEN
        result1.content.size shouldBe 10
        result2.content.size shouldBe 5
        result3.content.size shouldBe 10
    }

    @Test
    fun `SearchType이 NONE이 아닌 경우 Keyword 에 의해 검색되는지 결과 확인`() {
        // GIVEN
        todoRepository.saveAllAndFlush(DEFAULT_TODO_LIST)

        // WHEN
        val result = todoRepository.search(SearchType.TITLE, "10", Pageable.ofSize(10))

        // THEN
        result.content.size shouldBe 1
    }

    @Test
    fun `Keyword에 의해 조회된 결과가 0건일 경우 결과 확인`() {
        // GIVEN
        todoRepository.saveAllAndFlush(DEFAULT_TODO_LIST)

        // WHEN
        val result = todoRepository.search(SearchType.STATE, "TRUE", Pageable.ofSize(10))

        // THEN
        result.content.size shouldBe 0
    }

    @Test
    fun `조회된 결과가 10개, PageSize 5일 때 0Page 결과 확인`() {
        // GIVEN
        todoRepository.saveAllAndFlush(DEFAULT_TODO_LIST)

        // WHEN
        val result = todoRepository.search(SearchType.WRITER, "Writer", PageRequest.of(0, 6))

        // THEN
        result.content.size shouldBe 6
        result.isLast shouldBe false // 마지막 페이지인지 여부
        result.totalPages shouldBe 2 // 전체 페이지 수
        result.number shouldBe 0 // 현재 페이지 번호
        result.totalElements shouldBe 10 // 데이터의 총 개수(전체 페이지의 데이터 수)
    }

    @Test
    fun `조회된 결과가 10개, PageSize 5일 때 1Page 결과 확인`() {
        // GIVEN
        todoRepository.saveAllAndFlush(DEFAULT_TODO_LIST)

        // WHEN
        val result = todoRepository.search(SearchType.WRITER, "Writer", PageRequest.of(1, 6))

        // THEN
        result.content.size shouldBe 4
        result.isLast shouldBe true // 마지막 페이지인지 여부
        result.totalPages shouldBe 2 // 전체 페이지 수
        result.number shouldBe 1 // 현재 페이지 번호
        result.totalElements shouldBe 10 // 데이터의 총 개수(전체 페이지의 데이터 수)
    }

    companion object {
        private val user = User.from(
            CreateUserRequest(
                email = "test@test.com",
                nickname = "test-user",
                password = "test1234",
            ),
            joinType = "EMAIL"
        )

        private val DEFAULT_TODO_LIST = listOf(
            Todo.from(
                CreateTodoRequest(
                    title = "Todo 1",
                    content = "Content 1",
                    writer = "Writer 1"
                ), user = user
            ),
            Todo.from(
                CreateTodoRequest(
                    title = "Todo 2",
                    content = "Content 2",
                    writer = "Writer 2"
                ), user = user
            ),
            Todo.from(
                CreateTodoRequest(
                    title = "Todo 3",
                    content = "Content 3",
                    writer = "Writer 3"
                ), user = user
            ),
            Todo.from(
                CreateTodoRequest(
                    title = "Todo 4",
                    content = "Content 4",
                    writer = "Writer 41"
                ), user = user
            ),
            Todo.from(
                CreateTodoRequest(
                    title = "Todo 5",
                    content = "Content 5",
                    writer = "Writer 5"
                ), user = user
            ),
            Todo.from(
                CreateTodoRequest(
                    title = "Todo 6",
                    content = "Content 6",
                    writer = "Writer 6"
                ), user = user
            ),
            Todo.from(
                CreateTodoRequest(
                    title = "Todo 7",
                    content = "Content 7",
                    writer = "Writer 7"
                ), user = user
            ),
            Todo.from(
                CreateTodoRequest(
                    title = "Todo 8",
                    content = "Content 8",
                    writer = "Writer 8"
                ), user = user
            ),
            Todo.from(
                CreateTodoRequest(
                    title = "Todo 9",
                    content = "Content 9",
                    writer = "Writer 9"
                ), user = user
            ),
            Todo.from(
                CreateTodoRequest(
                    title = "Todo 10",
                    content = "Content 10",
                    writer = "Writer 10"
                ), user = user
            )
        )
    }
}