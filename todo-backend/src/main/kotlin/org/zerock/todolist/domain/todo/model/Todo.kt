package org.zerock.todolist.domain.todo.model

import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import org.zerock.todolist.domain.BaseEntity
import org.zerock.todolist.domain.comment.model.Comment
import org.zerock.todolist.domain.comment.model.toResponse
import org.zerock.todolist.domain.todo.dto.CreateTodoRequest
import org.zerock.todolist.domain.todo.dto.TodoListResponse
import org.zerock.todolist.domain.todo.dto.TodoResponse
import org.zerock.todolist.domain.user.model.User

@Entity
@SQLDelete(sql = "UPDATE todo SET is_deleted = true WHERE id = ?") // delete 쿼리 수행 시 update 처리
class Todo private constructor(
    title: String,
    content: String,
    writer: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    var title: String = title
        protected set
    var content: String = content
        protected set
    var writer: String = writer
        protected set

    @Enumerated(EnumType.STRING)
    var completed: TodoCompleted = TodoCompleted.FALSE
        protected set

    @Column(name = "is_deleted")
    var isDeleted: Boolean = false
        protected set

    @OneToMany(mappedBy = "todo", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    val comments: MutableList<Comment> = mutableListOf()

    companion object {
        fun from(createTodoRequest: CreateTodoRequest, user: User): Todo {
            return Todo(
                createTodoRequest.title,
                createTodoRequest.content,
                createTodoRequest.writer,
                user
            )
        }
    }

    fun update(title: String?, content: String?, writer: String?, completed: TodoCompleted?) {
        title?.let { this.title = it }
        content?.let { this.content = it }
        writer?.let { this.writer = it }
        completed?.let { this.completed = it }
    }
}

fun Todo.toResponse(): TodoResponse {
    return TodoResponse(id!!, title, content, writer, createdAt, completed, comments.map { it.toResponse() })
}

fun Todo.toListResponse(): TodoListResponse {
    return TodoListResponse(id!!, title, content, user.id!!, writer, createdAt, completed)
}