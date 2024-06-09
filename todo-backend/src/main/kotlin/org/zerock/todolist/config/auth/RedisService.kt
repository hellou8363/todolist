package org.zerock.todolist.config.auth

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class RedisService(
    private val redisTemplate: RedisTemplate<String, String>,
) {
    // Jwt Refresh Token을 저장(key: userId, value: refresh token)
    fun create(userId: String, refreshToken: String, hours: Long) {
        val valueOperations = redisTemplate.opsForValue()
        valueOperations.set(userId, refreshToken, Duration.ofHours(hours)) // token 만료일과 삭제일 동일
    }

    fun getRefreshToken(userId: String): String? {
        val valueOperations = redisTemplate.opsForValue()
        return valueOperations.get(userId)
    }

    fun delete(userId: String) {
        redisTemplate.delete(userId)
    }
}