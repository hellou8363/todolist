package org.zerock.todolist.domain.todo.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.zerock.todolist.domain.todo.dto.CreateTodoRequest
import org.zerock.todolist.domain.todo.dto.TodoListResponse
import org.zerock.todolist.domain.todo.dto.TodoResponse
import org.zerock.todolist.domain.todo.dto.UpdateTodoRequest
import org.zerock.todolist.domain.todo.type.SearchType

interface TodoService {

    fun getAllTodoList(searchType: SearchType, keyword: String, pageable: Pageable): Page<TodoListResponse>

    fun getTodoById(todoId: Long): TodoResponse

    fun createTodo(userId: Long, request: CreateTodoRequest): TodoResponse

    fun updateTodo(todoId: Long, userId: Long, request: UpdateTodoRequest): TodoResponse

    fun deleteTodo(todoId: Long, userId: Long)
}