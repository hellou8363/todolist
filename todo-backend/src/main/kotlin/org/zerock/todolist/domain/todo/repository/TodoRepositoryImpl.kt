package org.zerock.todolist.domain.todo.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import org.zerock.todolist.domain.comment.model.QComment
import org.zerock.todolist.domain.todo.model.QTodo
import org.zerock.todolist.domain.todo.model.Todo
import org.zerock.todolist.infra.querydsl.QueryDslSupport

@Repository // 외부 기능을 가져다 사용하는 것이기 때문에 어노테이션 필수
class TodoRepositoryImpl : QueryDslSupport(), CustomTodoRepository {

    private val todo = QTodo.todo
    private val comment = QComment.comment

    override fun searchTodoListByWriter(
        writer: String,
        pageable: Pageable
    ): Page<Todo> { // 작성자명의 일부라도 일치하면 모두 검색
        val totalCount = queryFactory.select(todo.count())
            .from(todo)
            .where(todo.writer.contains(writer))
            .fetchOne() ?: 0L

        val contents = queryFactory.selectFrom(todo)
            .where(todo.writer.contains(writer))
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        return PageImpl(contents, pageable, totalCount)
    }

    override fun findAllTodoList(
        pageable: Pageable
    ): Page<Todo> {
        val totalCount = queryFactory.select(todo.count())
            .from(todo)
            .fetchOne() ?: 0L

        val contents = queryFactory.selectFrom(todo)
            .leftJoin(todo.comments, comment)
            .fetchJoin()
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        return PageImpl(contents, pageable, totalCount)
    }
}