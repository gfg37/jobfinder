package com.example.jobfinder.dto

data class VacancyUpdateRequest(
    val title: String,
    val description: String,
    val requirements: String,
    val salary: String
)
