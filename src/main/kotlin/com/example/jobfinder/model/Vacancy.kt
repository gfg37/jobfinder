package com.example.jobfinder.model

import jakarta.persistence.*

@Entity
@Table(name = "vacancies")
data class Vacancy(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val title: String,
    val description: String,
    val requirements: String,
    val salary: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_id")
    val employer: User
)
