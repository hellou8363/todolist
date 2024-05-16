package org.zerock.todolist.domain.todo.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.zerock.todolist.domain.todo.dto.CreateTodoRequest
import org.zerock.todolist.domain.todo.dto.TodoResponse
import org.zerock.todolist.domain.todo.dto.UpdateTodoRequest
import org.zerock.todolist.domain.todo.model.Todo

interface TodoService {

    fun getAllTodoList(pageable: Pageable, writer: String?): Page<TodoResponse>

    fun getTodoById(todoId: Long): TodoResponse

    fun createTodo(request: CreateTodoRequest): TodoResponse

    fun updateTodo(todoId: Long, request: UpdateTodoRequest): TodoResponse

    fun deleteTodo(todoId: Long)
}