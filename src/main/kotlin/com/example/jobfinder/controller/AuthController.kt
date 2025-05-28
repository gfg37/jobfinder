package com.example.jobfinder.controller

import com.example.jobfinder.dto.AuthResponse
import com.example.jobfinder.dto.LoginRequest
import com.example.jobfinder.dto.RegisterRequest
import com.example.jobfinder.model.User
import com.example.jobfinder.security.JwtUtils
import com.example.jobfinder.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userService: UserService,
    private val jwtUtils: JwtUtils
) {

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<User> {
        val registeredUser = userService.register(request)
        return ResponseEntity.ok(registeredUser)
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<AuthResponse> {
        val user = userService.login(request.email, request.password)
        val token = jwtUtils.generateToken(user)
        return ResponseEntity.ok(AuthResponse(token))
    }

}
