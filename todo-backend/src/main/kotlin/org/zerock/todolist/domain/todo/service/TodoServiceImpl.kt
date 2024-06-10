package org.zerock.todolist.domain.todo.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.zerock.todolist.domain.exception.CustomAccessDeniedException
import org.zerock.todolist.domain.exception.ModelNotFoundException
import org.zerock.todolist.domain.todo.dto.CreateTodoRequest
import org.zerock.todolist.domain.todo.dto.TodoListResponse
import org.zerock.todolist.domain.todo.dto.TodoResponse
import org.zerock.todolist.domain.todo.dto.UpdateTodoRequest
import org.zerock.todolist.domain.todo.model.Todo
import org.zerock.todolist.domain.todo.model.toListResponse
import org.zerock.todolist.domain.todo.model.toResponse
import org.zerock.todolist.domain.todo.repository.TodoRepository
import org.zerock.todolist.domain.user.repository.UserRepository
import org.zerock.todolist.domain.user.service.UserService

@Service
class TodoServiceImpl(
    private val todoRepository: TodoRepository,
    private val userRepository: UserRepository,
    private val userService: UserService
) : TodoService {

    override fun getAllTodoList(pageable: Pageable, writer: String?): Page<TodoListResponse> {
        return if (writer != null) {
            todoRepository.searchTodoListByWriter(writer, pageable).map { it.toListResponse() }
        } else {
            todoRepository.findAll(pageable).map { it.toListResponse() }
//            todoRepository.findAllTodoList(pageable).map { it.toListResponse() } // STEP 4 과제용
        }
    }

    override fun getTodoById(todoId: Long): TodoResponse {
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw ModelNotFoundException("Todo", todoId)
        return todo.toResponse()
    }

    @Transactional
    override fun createTodo(request: CreateTodoRequest): TodoResponse {
        val user = userService.getUserDetails()
            .let { userRepository.findByIdOrNull(it) } ?: throw ModelNotFoundException("User", null)
        return todoRepository.save(
            Todo.from(
                request,
                user
            )
        ).toResponse()
    }

    @Transactional
    override fun updateTodo(todoId: Long, request: UpdateTodoRequest): TodoResponse {
        val user = userService.getUserDetails()
            .let { userRepository.findByIdOrNull(it) } ?: throw ModelNotFoundException("User", null)
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
    override fun deleteTodo(todoId: Long) {
        val user = userService.getUserDetails()
            .let { userRepository.findByIdOrNull(it) } ?: throw ModelNotFoundException("User", null)
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw ModelNotFoundException("Todo", todoId)

        if (todo.user != user) {
            throw CustomAccessDeniedException("You do not have access.")
        }

        todoRepository.delete(todo)
    }
}