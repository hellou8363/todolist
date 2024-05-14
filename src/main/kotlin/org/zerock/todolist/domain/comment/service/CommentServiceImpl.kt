package org.zerock.todolist.domain.comment.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.zerock.todolist.domain.comment.dto.CommentResponse
import org.zerock.todolist.domain.comment.dto.CreateCommentRequest
import org.zerock.todolist.domain.comment.dto.DeleteCommentRequest
import org.zerock.todolist.domain.comment.dto.UpdateCommentRequest
import org.zerock.todolist.domain.comment.model.Comment
import org.zerock.todolist.domain.comment.model.toResponse
import org.zerock.todolist.domain.comment.repository.CommentRepository
import org.zerock.todolist.domain.exception.ModelNotFoundException
import org.zerock.todolist.domain.todo.repository.TodoRepository

@Service
class CommentServiceImpl(
    private val commentRepository: CommentRepository,
    private val todoRepository: TodoRepository
) : CommentService {
    override fun getAllCommentList(): List<CommentResponse> {
        return commentRepository.findAll().map { it.toResponse() }.sortedBy { it.createdAt }.reversed()
    }

    override fun getCommentById(commentId: Long): CommentResponse {
        val comment = commentRepository.findByIdOrNull(commentId) ?: throw ModelNotFoundException("Comment", commentId)
        return comment.toResponse()
    }

    @Transactional
    override fun createComment(todoId: Long, request: CreateCommentRequest): CommentResponse {
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw ModelNotFoundException("Todo", todoId)
        return commentRepository.save(
            Comment(
                content = request.content,
                writer = request.writer,
                password = request.password,
                todo = todo
            )
        ).toResponse()
    }

    @Transactional
    override fun updateComment(todoId: Long, commentId: Long, request: UpdateCommentRequest): CommentResponse {
        val comment =
            commentRepository.findByTodoIdAndId(todoId, commentId) ?: throw ModelNotFoundException("Comment", commentId)

        if (comment.writer == request.writer && comment.password == request.password) {
            comment.content = request.content
            comment.writer = request.writer

            return commentRepository.save(comment).toResponse()
        } else {
            throw IllegalArgumentException("writer or password does not match.")

        }
    }

    @Transactional
    override fun deleteComment(todoId: Long, commentId: Long, request: DeleteCommentRequest) {
        val comment =
            commentRepository.findByTodoIdAndId(todoId, commentId) ?: throw ModelNotFoundException("Comment", commentId)

        if (comment.writer == request.writer && comment.password == request.password) {
            return commentRepository.delete(comment)
        } else {
            throw IllegalStateException("writer or password does not match.")
        }
    }
}