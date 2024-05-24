package org.zerock.todolist.domain.user.controller

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
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

    @PostMapping("/signin")
    fun `swagger-ui에 보여지기 위함`(@RequestBody signinRequest: SigninRequest) {}

//    fun signinUser(@RequestBody signinRequest: SigninRequest): ResponseEntity<UserResponse> {
//        val user = userService.getUserDetails()
//        return ResponseEntity.status(HttpStatus.OK).body(userService.signinUser(signinRequest))
//    }
}