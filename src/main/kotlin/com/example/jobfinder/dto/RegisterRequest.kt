package com.example.jobfinder.dto

import com.example.jobfinder.model.Role

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val role: Role
)
