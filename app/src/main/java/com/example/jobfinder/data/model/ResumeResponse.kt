package com.example.jobfinder.data.model

data class ResumeResponse(
    val id: Long,
    val position: String,
    val experience: String,
    val education: String,
    val skills: String,
    val userName: String
)
