package org.zerock.todolist.domain.todo.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import org.zerock.todolist.domain.todo.dto.CreateTodoRequest
import org.zerock.todolist.domain.todo.dto.TodoListResponse
import org.zerock.todolist.domain.todo.dto.TodoResponse
import org.zerock.todolist.domain.todo.dto.UpdateTodoRequest
import org.zerock.todolist.domain.todo.model.Todo
import org.zerock.todolist.domain.todo.model.toListResponse
import org.zerock.todolist.domain.todo.model.toResponse
import org.zerock.todolist.domain.todo.repository.TodoRepository
import org.zerock.todolist.domain.todo.type.SearchType
import org.zerock.todolist.domain.user.repository.UserRepository
import org.zerock.todolist.exception.CustomAccessDeniedException
import org.zerock.todolist.exception.ModelNotFoundException
import org.zerock.todolist.infra.aws.S3Service

@Service
class TodoServiceImpl(
    private val todoRepository: TodoRepository,
    private val userRepository: UserRepository,
    private val s3Service: S3Service
) : TodoService {

    override fun getAllTodoList(searchType: SearchType, keyword: String, pageable: Pageable): Page<TodoListResponse> {
        return todoRepository.search(searchType, keyword, pageable).map { it.toListResponse() }
    }

    override fun getTodoById(todoId: Long): TodoResponse {
        val todo = todoRepository.findByIdAndIsDeleted(todoId, false) ?: throw ModelNotFoundException("Todo", todoId)
        return todo.toResponse()
    }

    @Transactional
    override fun createTodo(userId: Long, request: CreateTodoRequest, file: MultipartFile?): TodoResponse {
        val user = userRepository.findByIdOrNull(userId) ?: throw ModelNotFoundException("User", userId)

        val result = todoRepository.save(
            Todo.from(
                request,
                user
            )
        )

        // file의 key로 todo id를 사용
        file?.let {
            result.imageUpload(s3Service.upload(it, result.id.toString()))
        }

        return result.toResponse()
    }

    @Transactional
    override fun updateTodo(todoId: Long, userId: Long, request: UpdateTodoRequest): TodoResponse {
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw ModelNotFoundException("Todo", todoId)

        if (todo.user.id != userId) {
            throw CustomAccessDeniedException("You do not have access.")
        }

        todo.update(
            title = request.title,
            content = request.content,
            writer = request.writer,
            completed = request.completed
        )

        return todo.toResponse()
    }

    @Transactional
    override fun deleteTodo(todoId: Long, userId: Long) {
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw ModelNotFoundException("Todo", todoId)

        if (todo.user.id != userId) {
            throw CustomAccessDeniedException("You do not have access.")
        }

        todoRepository.delete(todo)
    }
}