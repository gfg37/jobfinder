package com.example.jobfinder.model

import jakarta.persistence.*

@Entity
@Table(name = "resumes")
data class Resume(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val position: String,
    val experience: String,
    val education: String,
    val skills: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User
)
