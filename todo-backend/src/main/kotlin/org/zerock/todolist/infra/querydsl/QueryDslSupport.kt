package org.zerock.todolist.infra.querydsl

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext

abstract class QueryDslSupport { // 직접 사용하지 않으므로 추상 클래스로 만듦

    @PersistenceContext
    protected lateinit var entityManager: EntityManager

    protected val queryFactory: JPAQueryFactory
        get() = JPAQueryFactory(entityManager)
}