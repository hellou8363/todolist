package org.zerock.todolist.domain.user.controller

import jakarta.servlet.http.HttpSession
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.zerock.todolist.domain.user.dto.CreateUserRequest
import org.zerock.todolist.domain.user.dto.SigninRequest
import org.zerock.todolist.domain.user.dto.UserResponse
import org.zerock.todolist.domain.user.service.UserService

@RestController
class UserController(
    private val userService: UserService
) {

    @PostMapping("/signup")
    fun createUser(@Valid @RequestBody createUserRequest: CreateUserRequest): ResponseEntity<UserResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(createUserRequest))
    }

    // swagger에서 form으로 요청됨, SecurityConfig에 form -> json으로 변경하는 법을 공부해야 함
    @PostMapping("/signin", consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun signinUser(@RequestBody signinRequest: SigninRequest): ResponseEntity<UserResponse> {
        val user = userService.getUserDetails()
        return ResponseEntity.status(HttpStatus.OK).body(userService.signinUser(signinRequest))
    }
}