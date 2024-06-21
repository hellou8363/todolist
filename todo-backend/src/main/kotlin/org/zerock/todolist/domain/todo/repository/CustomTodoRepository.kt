package org.zerock.todolist.domain.todo.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.zerock.todolist.domain.todo.model.Todo
import org.zerock.todolist.domain.todo.type.SearchType

interface CustomTodoRepository {

    fun search(searchType: SearchType, keyword: String, pageable: Pageable): Page<Todo>
}