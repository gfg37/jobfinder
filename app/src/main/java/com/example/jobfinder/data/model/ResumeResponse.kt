package com.example.jobfinder.data.model

data class ResumeResponse(
    val id: Long,
    val title: String,
    val description: String,
    val skills: List<String>,
    val experience: String,
    val education: String,
    val ownerId: Long
)
