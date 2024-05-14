package org.zerock.todolist.domain.todo.controller

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.zerock.todolist.domain.todo.dto.CreateTodoRequest
import org.zerock.todolist.domain.todo.dto.TodoListResponse
import org.zerock.todolist.domain.todo.dto.TodoResponse
import org.zerock.todolist.domain.todo.dto.UpdateTodoRequest
import org.zerock.todolist.domain.todo.model.SortingStatus
import org.zerock.todolist.domain.todo.service.TodoService

@RestController
@RequestMapping("/todos")
class TodoController(
    private val todoService: TodoService
) {

    @GetMapping
    fun getTodoList(
        @RequestParam order: String = SortingStatus.DESC.name,
        @RequestParam writer: String? = null,
    ): ResponseEntity<List<TodoListResponse>> {
        return ResponseEntity.status(HttpStatus.OK).body(todoService.getAllTodoList(order, writer))
    }

    @GetMapping("/{todoId}")
    fun getTodo(@PathVariable todoId: Long): ResponseEntity<TodoResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(todoService.getTodoById(todoId))
    }

    @PostMapping
    fun createTodo(@Valid @RequestBody createTodoRequest: CreateTodoRequest): ResponseEntity<TodoResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(todoService.createTodo(createTodoRequest))
    }

    @PutMapping("/{todoId}")
    fun updateTodo(
        @PathVariable todoId: Long,
        @RequestBody updateTodoRequest: UpdateTodoRequest
    ): ResponseEntity<TodoResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(todoService.updateTodo(todoId, updateTodoRequest))
    }

    @DeleteMapping("/{todoId}")
    fun deleteTodo(@PathVariable todoId: Long): ResponseEntity<Unit> {
        todoService.deleteTodo(todoId)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}