package com.example.jobfinder.dto

data class VacancyRequest(
    val title: String,
    val description: String,
    val requirements: String,
    val salary: String
)
