package org.zerock.todolist.domain.todo.repository

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.zerock.todolist.domain.exception.ModelNotFoundException
import org.zerock.todolist.domain.todo.model.Todo
import org.zerock.todolist.domain.todo.model.TodoCompleted
import org.zerock.todolist.domain.todo.model.toResponse

@SpringBootTest
class TodoRepositoryTests {

    @Autowired
    private lateinit var todoRepository: TodoRepository

    @Test
    @DisplayName("10개의 데이터 삽입")
    fun insertTest() {
        for (i in 1..10) {
            val todo = Todo(title = "title...$i", content = "content...$i", writer = "writer...$i")
            todoRepository.save(todo)
        }
    }

    @Test
    @DisplayName("Todo 전체 목록 조회(작성일 기준 내림차순 정렬)")
    fun selectAllTest() {
        println(todoRepository.findAll().map { it.toResponse() }.sortedBy { it.createAt }.reversed())
    }

    @Test
    @DisplayName("단 건 Todo 조회")
    fun selectByIdTest() {
        val id = 123L
        val todo = todoRepository.findByIdOrNull(id) ?: throw ModelNotFoundException("Todo", id)
        println(todo.toResponse())
    }

    @Test
    @DisplayName("Todo 삭제")
    fun deleteTest() {
        val id = 124L
        val todo = todoRepository.findByIdOrNull(id) ?: throw ModelNotFoundException("Todo", id)
        todoRepository.delete(todo)
    }

    @Test
    @DisplayName("Todo 수정")
    fun updateTest() {
        val id = 123L
        val todo = todoRepository.findByIdOrNull(id) ?: throw ModelNotFoundException("Todo", id)

//        todo.title = "Todo 수정 테스트"
//        todo.content = "Todo 수정 테스트"
//        todo.writer = "Todo 수정 테스트"
        todo.completed = TodoCompleted.TRUE

        val result = todoRepository.save(todo)
        println(result.toResponse())
    }
}