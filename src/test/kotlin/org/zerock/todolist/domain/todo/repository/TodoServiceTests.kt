package org.zerock.todolist.domain.todo.repository

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.transaction.annotation.Transactional
import org.zerock.todolist.domain.todo.model.toResponse
import org.zerock.todolist.domain.todo.service.TodoService

@SpringBootTest
class TodoServiceTests {
    @Autowired
    private lateinit var todoService: TodoService

    @Test
    @Transactional
    fun `페이징 테스트`() {
        val pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"))
        val result = todoService.getAllTodoList(pageRequest, null)
        result.map { println(it) }
    }
}