package com.example.jobfinder.service

import com.example.jobfinder.dto.RegisterRequest
import com.example.jobfinder.dto.UserProfileResponse
import com.example.jobfinder.dto.UserProfileUpdateRequest
import com.example.jobfinder.model.User
import com.example.jobfinder.repository.UserRepository
import jakarta.transaction.Transactional
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
    fun getProfile(user: User): UserProfileResponse {
        return UserProfileResponse(
            id = user.id,
            name = user.name,
            email = user.email,
            role = user.role.name
        )
    }

    @Transactional
    fun updateProfile(user: User, request: UserProfileUpdateRequest): UserProfileResponse {
        val managedUser = userRepository.findById(user.id).orElseThrow()

        val updatedUser = managedUser.copy(
            name = request.name,
            password = passwordEncoder.encode(request.password)
        )
        userRepository.save(updatedUser)

        return UserProfileResponse(
            id = updatedUser.id,
            name = updatedUser.name,
            email = updatedUser.email,
            role = updatedUser.role.name
        )
    }

}
