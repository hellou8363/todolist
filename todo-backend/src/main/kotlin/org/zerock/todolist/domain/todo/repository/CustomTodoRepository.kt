package org.zerock.todolist.domain.todo.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.zerock.todolist.domain.todo.model.Todo

interface CustomTodoRepository {

    fun searchTodoListByWriter(writer: String, pageable: Pageable): Page<Todo>
}