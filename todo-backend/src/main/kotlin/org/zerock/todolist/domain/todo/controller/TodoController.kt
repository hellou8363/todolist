package org.zerock.todolist.domain.todo.controller

import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.zerock.todolist.domain.todo.dto.CreateTodoRequest
import org.zerock.todolist.domain.todo.dto.TodoListResponse
import org.zerock.todolist.domain.todo.dto.TodoResponse
import org.zerock.todolist.domain.todo.dto.UpdateTodoRequest
import org.zerock.todolist.domain.todo.service.TodoService
import org.zerock.todolist.domain.user.service.UserService

@RestController
@RequestMapping("/todos")
class TodoController(
    private val todoService: TodoService,
    private val userService: UserService
) {
    @GetMapping
    fun getTodoList(
        @RequestParam writer: String? = null,
        @PageableDefault(page = 0, size = 10, sort = ["id"], direction = Sort.Direction.DESC) pageable: Pageable
    ): ResponseEntity<Page<TodoListResponse>> {
        return ResponseEntity.status(HttpStatus.OK).body(todoService.getAllTodoList(pageable, writer))
    }

    @GetMapping("/{todoId}")
    fun getTodo(@PathVariable todoId: Long): ResponseEntity<TodoResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(todoService.getTodoById(todoId))
    }

    @PostMapping
    fun createTodo(@Valid @RequestBody createTodoRequest: CreateTodoRequest): ResponseEntity<TodoResponse> {
        val userEmail = userService.getUserDetails()?.username
        return ResponseEntity.status(HttpStatus.CREATED).body(todoService.createTodo(createTodoRequest, userEmail))
    }

    @PutMapping("/{todoId}")
    fun updateTodo(
        @PathVariable todoId: Long,
        @RequestBody updateTodoRequest: UpdateTodoRequest
    ): ResponseEntity<TodoResponse> {
        val userEmail = userService.getUserDetails()?.username
        return ResponseEntity.status(HttpStatus.OK).body(todoService.updateTodo(todoId, updateTodoRequest, userEmail))
    }

    @DeleteMapping("/{todoId}")
    fun deleteTodo(@PathVariable todoId: Long): ResponseEntity<Unit> {
        val userEmail = userService.getUserDetails()?.username
        todoService.deleteTodo(todoId, userEmail)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}