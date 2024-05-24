package org.zerock.todolist.domain.user.repository


import org.springframework.data.jpa.repository.JpaRepository
import org.zerock.todolist.domain.user.model.User

interface UserRepository : JpaRepository<User, Long>{

    fun existsByEmail(email: String): Boolean
    fun findByEmail(email: String): User
}