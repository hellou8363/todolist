package org.zerock.todolist.domain.todo.controller

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.zerock.todolist.domain.todo.dto.TodoResponse
import org.zerock.todolist.domain.todo.model.TodoCompleted
import org.zerock.todolist.domain.todo.service.TodoService
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockKExtension::class) // io.mockk.junit5.MockKExtension
class TodoControllerTests @Autowired constructor(
    private val mockMvc: MockMvc
) : DescribeSpec({
    extension(SpringExtension) // io.kotest.extensions.spring.SpringExtension

    afterContainer { // Mocking Clear
        clearAllMocks()
    }

    val todoService = mockk<TodoService>()

    describe("GET /todos/{id}") {
        context("존재하는 ID를 요청할 때") {
            it("200 status code를 응답한다.") {
                val todoId = 1L


                every { todoService.getTodoById(any()) } returns TodoResponse(
                    id = todoId,
                    title = "test_title",
                    content = "test_content",
                    writer = "test_writer",
                    createdAt = LocalDateTime.now(),
                    completed = TodoCompleted.FALSE,
                    comments = mutableListOf()
                )

                val result = mockMvc.perform(
                    get("/todos/$todoId") // org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
                        .contentType(MediaType.APPLICATION_JSON)
                ).andReturn()

                result.response.status shouldBe 200

                val DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSSSSS"

                val responseDto = jacksonObjectMapper().registerModule(
                    JavaTimeModule().addSerializer(
                        LocalDateTime::class.java, LocalDateTimeSerializer(
                            DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)
                        )
                    )
                ).readValue(
                    result.response.contentAsString,
                    TodoResponse::class.java
                )

                responseDto.id shouldBe todoId
            }
        }
    }
})