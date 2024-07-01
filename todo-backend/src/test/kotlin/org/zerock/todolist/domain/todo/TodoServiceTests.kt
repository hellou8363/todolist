package org.zerock.todolist.domain.todo

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import org.zerock.todolist.domain.todo.dto.CreateTodoRequest
import org.zerock.todolist.domain.todo.model.Todo
import org.zerock.todolist.domain.todo.repository.TodoRepository
import org.zerock.todolist.domain.todo.service.TodoService
import org.zerock.todolist.domain.todo.service.TodoServiceImpl
import org.zerock.todolist.domain.user.dto.CreateUserRequest
import org.zerock.todolist.domain.user.model.User
import org.zerock.todolist.domain.user.repository.UserRepository
import org.zerock.todolist.exception.ModelNotFoundException
import org.zerock.todolist.infra.aws.S3Service


class TodoServiceTests : BehaviorSpec({

    val todoRepository: TodoRepository = mockk<TodoRepository>()
    val userRepository: UserRepository = mockk<UserRepository>()
    val s3Service: S3Service = mockk<S3Service>()
    val todoService = TodoServiceImpl(todoRepository, userRepository, s3Service)

    val savedTodo = Todo.from(
        CreateTodoRequest(
            title = "title",
            content = "content",
            writer = "writer"
        ),
        user = User.from(
            CreateUserRequest(
                email = "test@test.com",
                nickname = "yoyo",
                password = "1234"
            ), joinType = "EMAIL"
        )
    )

    savedTodo.id = 1L // 테스트를 위해 Todo Entity의 id를 val -> var로 변경

    every { todoRepository.findByIdAndIsDeleted(1L, false) } returns savedTodo
    every { todoRepository.findByIdAndIsDeleted(10L, false) } returns null


    Given("todo가 존재할 때") {
        When("해당 todo를 요청하면") {
            val targetTodoId = 1L
            val result = todoService.getTodoById(targetTodoId)

            Then("todo 정보로 응답한다.") {
                result shouldNotBe null
                result.let {
                    it.id shouldBe savedTodo.id
                    it.title shouldBe savedTodo.title
                    it.content shouldBe savedTodo.content
                    it.writer shouldBe savedTodo.writer
                }
            }
        }
    }

    Given("존재하지 않는 todo에 대해") {
        When("해당 todo를 요청하면") {
            Then("ModelNotFoundException이 발생해야 한다.") {
                val nonTargetTodoId = 10L

                shouldThrow<ModelNotFoundException> {
                    todoService.getTodoById(nonTargetTodoId)
                }
            }
        }
    }
})