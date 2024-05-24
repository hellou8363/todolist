package org.zerock.todolist.domain.todo.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.zerock.todolist.domain.exception.CustomAccessDeniedException
import org.zerock.todolist.domain.exception.ModelNotFoundException
import org.zerock.todolist.domain.todo.dto.CreateTodoRequest
import org.zerock.todolist.domain.todo.dto.TodoResponse
import org.zerock.todolist.domain.todo.dto.UpdateTodoRequest
import org.zerock.todolist.domain.todo.model.Todo
import org.zerock.todolist.domain.todo.model.toResponse
import org.zerock.todolist.domain.todo.repository.TodoRepository
import org.zerock.todolist.domain.user.model.User
import org.zerock.todolist.domain.user.repository.UserRepository

@Service
class TodoServiceImpl(
    private val todoRepository: TodoRepository,
    private val userRepository: UserRepository
) : TodoService {

    override fun getAllTodoList(pageable: Pageable, writer: String?): Page<TodoResponse> {
        if (writer != null) {
            return todoRepository.findByWriter(writer, pageable).map { it.toResponse() }
        } else {
            return todoRepository.findAll(pageable).map { it.toResponse() }
        }
    }

    override fun getTodoById(todoId: Long): TodoResponse {
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw ModelNotFoundException("Todo", todoId)
        return todo.toResponse()
    }

    @Transactional
    override fun createTodo(request: CreateTodoRequest, userEmail: String?): TodoResponse {
        val user = userEmail?.let { userRepository.findByEmail(it) } ?: throw ModelNotFoundException("User", null)
        return todoRepository.save(
            Todo(
                title = request.title,
                content = request.content,
                writer = request.writer,
                user = user
            )
        ).toResponse()
    }

    @Transactional
    override fun updateTodo(todoId: Long, request: UpdateTodoRequest, userEmail: String?): TodoResponse {
        val user = userEmail?.let { userRepository.findByEmail(it) } ?: throw ModelNotFoundException("User", null)
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw ModelNotFoundException("Todo", todoId)

        if (todo.user != user) {
            throw CustomAccessDeniedException("You do not have access.")
        }
        val (title, content, writer) = request

        todo.title = title
        todo.content = content
        todo.writer = writer

        return todoRepository.save(todo).toResponse()
    }

    @Transactional
    override fun deleteTodo(todoId: Long, userEmail: String?) {
        val user = userEmail?.let { userRepository.findByEmail(it) } ?: throw ModelNotFoundException("User", null)
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw ModelNotFoundException("Todo", todoId)

        if (todo.user != user) {
            throw CustomAccessDeniedException("You do not have access.")
        }

        todoRepository.delete(todo)
    }
}