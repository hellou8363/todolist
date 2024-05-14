package org.zerock.todolist.domain.todo.service

import org.zerock.todolist.domain.todo.dto.CreateTodoRequest
import org.zerock.todolist.domain.todo.dto.TodoListResponse
import org.zerock.todolist.domain.todo.dto.TodoResponse
import org.zerock.todolist.domain.todo.dto.UpdateTodoRequest

interface TodoService {

    fun getAllTodoList(order: String?): List<TodoListResponse>

    fun getTodoById(todoId: Long): TodoResponse

    fun createTodo(request: CreateTodoRequest): TodoResponse

    fun updateTodo(todoId: Long, request: UpdateTodoRequest): TodoResponse

    fun deleteTodo(todoId: Long)
}