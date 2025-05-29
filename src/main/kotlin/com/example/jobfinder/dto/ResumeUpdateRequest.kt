package com.example.jobfinder.dto

data class ResumeUpdateRequest(
    val position: String,
    val experience: String,
    val education: String,
    val skills: String
)
