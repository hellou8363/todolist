package org.zerock.todolist.infra.querydsl

import com.querydsl.core.types.Expression
import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.EntityPathBase
import com.querydsl.core.types.dsl.PathBuilder
import org.springframework.data.domain.Sort

class QueryDslUtil {

    companion object {
        fun getOrderSpecifier(qEntity: EntityPathBase<*>, sort: Sort): ArrayList<OrderSpecifier<*>> {
            val orders = arrayListOf<OrderSpecifier<*>>()

            sort.forEach {
                orders.add(
                    OrderSpecifier(
                        if (it.isAscending) Order.ASC else Order.DESC,
                        PathBuilder(qEntity.type, qEntity.metadata)
                            .get(it.property) as Expression<out Comparable<*>>
                    )
                )
            }

            return orders
        }
    }
}