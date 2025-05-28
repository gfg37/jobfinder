package com.example.jobfinder.service

import com.example.jobfinder.dto.RegisterRequest
import com.example.jobfinder.model.User
import com.example.jobfinder.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun register(request: RegisterRequest): User {
        if (userRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("Email уже зарегистрирован")
        }
        val user = User(
            name = request.name,
            email = request.email,
            password = passwordEncoder.encode(request.password),
            role = request.role
        )
        return userRepository.save(user)
    }

    fun login(email: String, password: String): User {
        val user = userRepository.findByEmail(email)
            ?: throw IllegalArgumentException("Неверный email или пароль")
        if (!passwordEncoder.matches(password, user.password)) {
            throw IllegalArgumentException("Неверный email или пароль")
        }
        return user
    }


}
