package org.zerock.todolist.domain.comment.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.zerock.todolist.domain.comment.dto.CommentResponse
import org.zerock.todolist.domain.comment.dto.CreateAndUpdateCommentRequest
import org.zerock.todolist.domain.comment.dto.DeleteCommentRequest
import org.zerock.todolist.domain.comment.model.Comment
import org.zerock.todolist.domain.comment.model.toResponse
import org.zerock.todolist.domain.comment.repository.CommentRepository
import org.zerock.todolist.domain.exception.CustomAccessDeniedException
import org.zerock.todolist.domain.exception.ModelNotFoundException
import org.zerock.todolist.domain.todo.repository.TodoRepository
import org.zerock.todolist.domain.user.repository.UserRepository

@Service
class CommentServiceImpl(
    private val commentRepository: CommentRepository,
    private val todoRepository: TodoRepository,
    private val userRepository: UserRepository,
) : CommentService {
    override fun getAllCommentList(): List<CommentResponse> {
        return commentRepository.findAll().map { it.toResponse() }.sortedBy { it.createdAt }.reversed()
    }

    override fun getCommentById(commentId: Long): CommentResponse {
        val comment = commentRepository.findByIdOrNull(commentId) ?: throw ModelNotFoundException("Comment", commentId)
        return comment.toResponse()
    }

    @Transactional
    override fun createComment(todoId: Long, request: CreateAndUpdateCommentRequest, userEmail: String?): CommentResponse {
        val user = userEmail?.let { userRepository.findByEmail(it) } ?: throw ModelNotFoundException("User", null)
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw ModelNotFoundException("Todo", todoId)

        return commentRepository.save(
            Comment(
                content = request.content,
                writer = request.writer,
                password = request.password,
                todo = todo,
                user = user
            )
        ).toResponse()
    }

    @Transactional
    override fun updateComment(todoId: Long, commentId: Long, request: CreateAndUpdateCommentRequest, userEmail: String?): CommentResponse {
        val user = userEmail?.let { userRepository.findByEmail(it) } ?: throw ModelNotFoundException("User", null)
        val comment =
            commentRepository.findByTodoIdAndId(todoId, commentId) ?: throw ModelNotFoundException("Comment", commentId)

        if (comment.user != user) {
            throw CustomAccessDeniedException("You do not have access.")
        }

        if (comment.writer == request.writer && comment.password == request.password) {
            comment.content = request.content
            comment.writer = request.writer

            return commentRepository.save(comment).toResponse()
        } else {
            throw IllegalArgumentException("writer or password does not match.")

        }
    }

    @Transactional
    override fun deleteComment(todoId: Long, commentId: Long, request: DeleteCommentRequest, userEmail: String?) {
        val user = userEmail?.let { userRepository.findByEmail(it) } ?: throw ModelNotFoundException("User", null)
        val comment =
            commentRepository.findByTodoIdAndId(todoId, commentId) ?: throw ModelNotFoundException("Comment", commentId)

        if (comment.user != user) {
            throw CustomAccessDeniedException("You do not have access.")
        }

        if (comment.writer == request.writer && comment.password == request.password) {
            return commentRepository.delete(comment)
        } else {
            throw IllegalArgumentException("writer or password does not match.")
        }
    }
}