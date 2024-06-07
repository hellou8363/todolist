package org.zerock.todolist.domain.user.controller

import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.zerock.todolist.domain.user.dto.CreateUserRequest
import org.zerock.todolist.domain.user.dto.SigninRequest
import org.zerock.todolist.domain.user.dto.UserResponse
import org.zerock.todolist.domain.user.service.UserService

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    @PostMapping("/signup")
    fun createUser(@Valid @RequestBody createUserRequest: CreateUserRequest): ResponseEntity<UserResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(createUserRequest))
    }

    @PostMapping("/signin")
    fun `swagger-ui에 보여지기 위함`(@RequestBody signinRequest: SigninRequest) {
    }

    @GetMapping("/signin/kakao")
    fun signinKakao(@RequestParam accessToken: String, response: HttpServletResponse) {
        log.info("access token: {}", accessToken)
        userService.signWithKakao(accessToken, response)
//        return ResponseEntity.status(HttpStatus.CREATED).body(userService.getUserFromKakao(accessToken))
    }

    @GetMapping("/signin/naver")
    fun signinNaver(@RequestParam accessToken: String) { // : ResponseEntity<UserResponse>
        log.info("access token: {}", accessToken)
        userService.getUserFromNaver(accessToken)
    }

    @GetMapping("/refresh")
    fun refreshUser(
        @RequestHeader(name = "Authorization") accessToken: String,
        @CookieValue(name = "TODOLIST_REFRESHTOKEN", required = false) refreshToken: String?
//        @RequestHeader(name = "TODOLIST_REFRESHTOKEN") refreshToken: String
    ) {
        log.info("***** refereshUser")
        log.info("access token: {}", accessToken)
        log.info("refresh token: {}", refreshToken)
    }
}