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

    // STEP 1에서 작성일 기준 내림차순이 기본 정렬, STEP 3에서 오름차순, 내림차순 값을 받지 않았을 때의 기본 정렬을 따르는지 예외를 던져야 하는지 모르겠음
    override fun getAllTodoList(order: String?): List<TodoListResponse> {
        val value = SortingStatus.values().any { it.name == order }

        if(!value) { // ASC, DESC가 아닌 경우 예외 발생
            throw IllegalArgumentException("Input must be either asc or desc.")
        }

        val ascendingOrder = todoRepository.findAll().map { it.toMultiResponse() }.sortedBy { it.createAt }

        if (order == SortingStatus.ASC.name) { // 기본 정렬이 날짜 기준 내림차순이므로 오름차순 여부만 검사, 아니면 기본 정렬
            return ascendingOrder
        }

        return ascendingOrder.reversed()
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
}