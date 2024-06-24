package org.zerock.todolist.domain.todo

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.http.HttpStatus
import org.zerock.todolist.domain.todo.controller.TodoController
import org.zerock.todolist.domain.todo.dto.TodoResponse
import org.zerock.todolist.domain.todo.model.TodoCompleted
import org.zerock.todolist.domain.todo.service.TodoService
import org.zerock.todolist.exception.ModelNotFoundException
import java.time.LocalDateTime


class TodoControllerTest : BehaviorSpec({
    val savedTodoId = 1L
    val notSavedTodoId = 10L
    val todoService = mockk<TodoService>()
    val todoController = mockk<TodoController>()

    every { todoService.getTodoById(savedTodoId) } returns TodoResponse(
        id = savedTodoId,
        title = "title",
        content = "content",
        writer = "writer",
        createdAt = LocalDateTime.of(2024, 6, 24, 12, 12, 12),
        completed = TodoCompleted.FALSE,
        comments = emptyList()
    )

//    every { todoService.getTodoById(notSavedTodoId) } returns throw ModelNotFoundException("Todo", notSavedTodoId)

    Given("saved todo id") {
        val targetTodoId = savedTodoId

        When("find todo id") {
            val result = todoController.getTodo(savedTodoId)

            Then("status code should be ok") {
                result.statusCode shouldBe HttpStatus.OK
                result.body?.id shouldBe savedTodoId
            }
        }
    }

//    Given("not saved todo id") {
//        val targetTodoId = notSavedTodoId
//        When("find todo id") {
//            val result = todoController.getTodo(targetTodoId)
//            Then("status code should be not found") {
//                result.statusCode shouldBe HttpStatus.NOT_FOUND
//                result.body shouldBe null
//            }
//        }
//    }


})