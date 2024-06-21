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
    fun signin(@RequestBody signinRequest: SigninRequest, response: HttpServletResponse) {
        userService.signinUser(signinRequest, response)
    }

    @GetMapping("/signin/kakao")
    fun signinKakao(@RequestParam accessToken: String, response: HttpServletResponse) { // 소셜 로그인은 Controller에서 반환하는 값 X
        userService.signinWithKakao(accessToken, response)
    }

    // TODO: 프런트 작업 후 진행 - 프런트에서 액세스 토큰까지 발급 후 넘겨주는 것으로 하려고 미구현
    @GetMapping("/signin/naver")
    fun signinNaver(@RequestParam accessToken: String) {
        log.info("access token: {}", accessToken)
        userService.getUserFromNaver(accessToken)
    }

    @GetMapping("/refresh")
    fun refreshToken(
        @RequestHeader(name = "Authorization") accessToken: String,
        @CookieValue(name = "TODOLIST_REFRESHTOKEN") refreshToken: String,
        response: HttpServletResponse
    ) {
        userService.refresh(accessToken, refreshToken, response)
    }

    @GetMapping("/logout")
    fun logout(@RequestHeader(name = "userId") userId: String, response: HttpServletResponse) {
        userService.logoutUser(userId, response)
    }
}