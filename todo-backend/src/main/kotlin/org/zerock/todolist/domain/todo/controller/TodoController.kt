package org.zerock.todolist.domain.todo.controller

import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.zerock.todolist.domain.todo.dto.CreateTodoRequest
import org.zerock.todolist.domain.todo.dto.TodoListResponse
import org.zerock.todolist.domain.todo.dto.TodoResponse
import org.zerock.todolist.domain.todo.dto.UpdateTodoRequest
import org.zerock.todolist.domain.todo.service.TodoService
import org.zerock.todolist.infra.aop.StopWatch

@RestController
@RequestMapping("/todos")
class TodoController(
    private val todoService: TodoService
) {
    @StopWatch
    @GetMapping
    fun getTodoList(
        @RequestParam writer: String? = null,
        @PageableDefault(page = 0, size = 10, sort = ["id"], direction = Sort.Direction.DESC) pageable: Pageable
    ): ResponseEntity<Page<TodoListResponse>> {
        return ResponseEntity.status(HttpStatus.OK).body(todoService.getAllTodoList(pageable, writer))
    }

    @StopWatch
    @GetMapping("/{todoId}")
    fun getTodo(@PathVariable todoId: Long): ResponseEntity<TodoResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(todoService.getTodoById(todoId))
    }

    @PostMapping
    fun createTodo(
        @AuthenticationPrincipal userId: Long,
        @Valid @RequestBody createTodoRequest: CreateTodoRequest,
    ): ResponseEntity<TodoResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(todoService.createTodo(userId, createTodoRequest))
    }

    @PutMapping("/{todoId}")
    fun updateTodo(
        @AuthenticationPrincipal userId: Long,
        @PathVariable todoId: Long,
        @RequestBody updateTodoRequest: UpdateTodoRequest
    ): ResponseEntity<TodoResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(todoService.updateTodo(todoId, userId, updateTodoRequest))
    }

    @DeleteMapping("/{todoId}")
    fun deleteTodo(
        @AuthenticationPrincipal userId: Long,
        @PathVariable todoId: Long
    ): ResponseEntity<Unit> {
        todoService.deleteTodo(todoId, userId)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}