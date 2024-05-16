package org.zerock.todolist.domain.todo.repository

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.support.PagedListHolder.DEFAULT_PAGE_SIZE
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
    fun `offset 기반 페이징 테스트`() {
        val pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"))
        val result = todoService.getAllTodoList(pageRequest, null)
        result.map { println(it) }
    }

    @Test
    @Transactional
    fun `cursor 기반 페이징 테스트`() {
//        val cursor = 25L
//        val pageRequest = PageRequest.of(0, DEFAULT_PAGE_SIZE)
//        val result = todoService.getAllTodoList(cursor, pageRequest, null)
//        result.map { println(it) }
//        pageRequest.pageSize
    }
}