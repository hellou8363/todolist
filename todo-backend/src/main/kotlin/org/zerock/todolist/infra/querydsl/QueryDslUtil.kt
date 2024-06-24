package org.zerock.todolist.infra.querydsl

import com.querydsl.core.types.Expression
import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.EntityPathBase
import com.querydsl.core.types.dsl.PathBuilder
import org.springframework.data.domain.Sort

class QueryDslUtil {

    companion object {
        fun getOrderSpecifier(qEntity: EntityPathBase<*>, sort: Sort): Array<OrderSpecifier<*>> {
            val orders = arrayListOf<OrderSpecifier<*>>()

            sort.forEach {
                orders.add(
                    OrderSpecifier(
                        if (it.isAscending) Order.ASC else Order.DESC,
                        PathBuilder(qEntity.type, qEntity.metadata)
                            .get(it.property) as Expression<out Comparable<*>> // OrderSpecifier<T extends Comparable>
                    )
                )
            }

            return orders.toTypedArray()
        }
    }
}