package org.zerock.todolist.domain.todo.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.zerock.todolist.domain.exception.ModelNotFoundException
import org.zerock.todolist.domain.todo.dto.CreateTodoRequest
import org.zerock.todolist.domain.todo.dto.TodoListResponse
import org.zerock.todolist.domain.todo.dto.TodoResponse
import org.zerock.todolist.domain.todo.dto.UpdateTodoRequest
import org.zerock.todolist.domain.todo.model.SortingStatus
import org.zerock.todolist.domain.todo.model.Todo
import org.zerock.todolist.domain.todo.model.toMultiResponse
import org.zerock.todolist.domain.todo.model.toResponse
import org.zerock.todolist.domain.todo.repository.TodoRepository

@Service
class TodoServiceImpl(
    private val todoRepository: TodoRepository
) : TodoService {

    override fun getAllTodoList(order: String, writer: String?): List<TodoListResponse> {
        if (writer != null) {
            return todoRepository.findByWriter(writer).map { it.toMultiResponse() }
        } else {
            val value = SortingStatus.values().any { it.name == order }

            if (!value) {
                throw IllegalArgumentException("Input must be either ASC or DESC.")
            }

            val ascendingOrder = todoRepository.findAll().map { it.toMultiResponse() }.sortedBy { it.createAt }

            if (order == SortingStatus.ASC.name) {
                return ascendingOrder
            }

            return ascendingOrder.reversed()
        }
    }

    override fun getTodoById(todoId: Long): TodoResponse {
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw ModelNotFoundException("Todo", todoId)
        return todo.toResponse()
    }

    @Transactional
    override fun createTodo(request: CreateTodoRequest): TodoResponse {
        return todoRepository.save(
            Todo(
                title = request.title,
                content = request.content,
                writer = request.writer
            )
        ).toResponse()
    }

    @Transactional
    override fun updateTodo(todoId: Long, request: UpdateTodoRequest): TodoResponse {
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw ModelNotFoundException("Todo", todoId)
        val (title, content, writer) = request

        todo.title = title
        todo.content = content
        todo.writer = writer

        return todoRepository.save(todo).toResponse()
    }

    @Transactional
    override fun deleteTodo(todoId: Long) {
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw ModelNotFoundException("Todo", todoId)
        todoRepository.delete(todo)
    }

    override fun searchTodo(todoId: Long, query: String?): List<TodoResponse> {
        TODO("Not yet implemented")
    }
}