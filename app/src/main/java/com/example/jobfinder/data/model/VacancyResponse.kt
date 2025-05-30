
package com.example.jobfinder.data.model

data class VacancyResponse(
    val id: Long,
    val title: String,
    val description: String,
    val requirements: String,
    val salary: String,
    val employerUsername: String
)
