package com.example.jobfinder.dto

data class VacancyResponse(
    val id: Long,
    val title: String,
    val description: String,
    val requirements: String,
    val salary: String,
    val employerName: String
)
