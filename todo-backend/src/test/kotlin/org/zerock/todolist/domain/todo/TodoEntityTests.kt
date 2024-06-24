package org.zerock.todolist.domain.todo

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.zerock.todolist.domain.todo.dto.CreateTodoRequest
import org.zerock.todolist.domain.todo.model.Todo
import org.zerock.todolist.domain.user.dto.CreateUserRequest
import org.zerock.todolist.domain.user.model.User

class TodoEntityTest : BehaviorSpec({

    val user = User.from(
        CreateUserRequest(
            email = "test@naver.com",
            nickname = "yoyo",
            password = "1234"
        ), joinType = "EMAIL"
    )

    Given("Todo Information") {
        val title = "test-title"
        val content = "test-content"
        val writer = "test-writer"

        When("Todo Constructor") {
            val result = Todo.from(
                CreateTodoRequest(
                    title = title,
                    content = content,
                    writer = writer
                ),
                user = user
            )

            then("Result Should be") {
                result.title shouldBe title
                result.content shouldBe content
                result.writer shouldBe writer
            }
        }
    }
})