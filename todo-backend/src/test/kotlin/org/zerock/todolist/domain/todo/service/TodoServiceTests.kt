package org.zerock.todolist.domain.todo.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.zerock.todolist.domain.exception.ModelNotFoundException
import org.zerock.todolist.domain.todo.repository.TodoRepository
import org.zerock.todolist.domain.user.repository.UserRepository

@SpringBootTest
@ExtendWith(MockKExtension::class)
class TodoServiceTests : BehaviorSpec({
    extension(SpringExtension)

    afterContainer {
        clearAllMocks()
    }

    val todoRepository: TodoRepository = mockk<TodoRepository>()
    val userRepository: UserRepository = mockk<UserRepository>()

    // 생성자 주입 방식을 사용하기 때문에 빈 객체로 바로 만들 수 있음(필드 기반 주입은 X)
    val todoService: TodoService = TodoServiceImpl(todoRepository, userRepository)

    Given("Todo 목록이 존재하지 않을 때") {
        When("특정 Todo를 요청하면") {
            Then("ModelNotFoundException이 발생해야 한다.") {
                // given
                val todoId = 100L

                every { todoRepository.findByIdOrNull(todoId) } returns null

                val result = shouldThrow<ModelNotFoundException> {
                    todoService.getTodoById(todoId)
                }

                println("result: $result")
            }
        }
    }
})