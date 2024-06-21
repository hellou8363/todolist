package org.zerock.todolist.domain.todo.repository

import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import org.zerock.todolist.domain.todo.model.QTodo
import org.zerock.todolist.domain.todo.model.Todo
import org.zerock.todolist.domain.todo.model.TodoCompleted
import org.zerock.todolist.domain.todo.type.SearchType

@Repository // 외부 기능을 가져다 사용하는 것이기 때문에 어노테이션 필수
class TodoRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : CustomTodoRepository {
    private val todo = QTodo.todo

    override fun search(searchType: SearchType, keyword: String, pageable: Pageable): Page<Todo> {
        val where = when (searchType) {
            SearchType.NONE -> null
            SearchType.TITLE -> todo.title.like("%$keyword%")
            SearchType.WRITER -> todo.writer.like("%$keyword%")
            SearchType.STATE -> todo.completed.eq(TodoCompleted.valueOf(keyword))
            SearchType.DAYSAGO ->
                Expressions.currentTimestamp().dayOfMonth().subtract(todo.createdAt.dayOfMonth()).eq(keyword.toInt())
        }

        val totalCount = queryFactory.select(todo.count())
            .from(todo)
            .where(where)
            .fetchOne() ?: 0L

        val contents = queryFactory.selectFrom(todo)
            .where(where)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        return PageImpl(contents, pageable, totalCount)
    }
}