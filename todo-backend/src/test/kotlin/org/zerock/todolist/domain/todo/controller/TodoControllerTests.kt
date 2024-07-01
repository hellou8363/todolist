package org.zerock.todolist.domain.todo.controller

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.zerock.todolist.domain.todo.dto.TodoResponse
import org.zerock.todolist.domain.todo.model.TodoCompleted
import org.zerock.todolist.domain.todo.service.TodoService
import org.zerock.todolist.exception.GlobalExceptionHandler
import org.zerock.todolist.exception.ModelNotFoundException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@WebMvcTest(TodoController::class)
@ExtendWith(MockKExtension::class)
@WithMockUser
@AutoConfigureMockMvc
@ImportAutoConfiguration(GlobalExceptionHandler::class)
class TodoControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,

    @MockkBean
    private val todoService: TodoService
) : DescribeSpec({
    extension(SpringExtension)

    afterContainer {
        clearAllMocks()
    }

//    val mock: MockMvc = MockMvcBuilders.standaloneSetup(TodoController(todoService))
//        .setControllerAdvice(GlobalExceptionHandler())
//        .build()

    describe("GET /todos/{id}") {
        context("존재하는 ID를 요청할 때") {
            it("Status Code 200을 응답한다.") {
                val todoId = 1L

                every { todoService.getTodoById(any()) } returns TodoResponse(
                    id = todoId,
                    title = "title",
                    content = "content",
                    writer = "writer",
                    createdAt = LocalDateTime.of(2024, 6, 24, 12, 12, 12),
                    completed = TodoCompleted.FALSE,
                    comments = emptyList(),
                    imageLink = null
                )

                val result = mockMvc.perform(
                    get("/todos/$todoId") // org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                ).andReturn()

                result.response.status shouldBe 200

                val responseDto = jacksonObjectMapper().registerModule(
                    JavaTimeModule().addSerializer(
                        LocalDateTime::class.java, LocalDateTimeSerializer(
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")
                        )
                    )
                ).readValue(
                    result.response.contentAsString,
                    TodoResponse::class.java
                )

                responseDto.id shouldBe todoId
            }
        }

//        context("존재하지 않는 ID를 요청할 때") {
//            it("Status Code 400을 응답한다.") {
//                val todoId = 10L
//
//                every { todoService.getTodoById(any()) } returns throw ModelNotFoundException("Todo", todoId)
//
//                val result = mockMvc.perform(
//                    get("/todos/$todoId") // org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                ).andReturn()
//
//                result.response.status shouldBe 400
//            }
//        }
    }
})