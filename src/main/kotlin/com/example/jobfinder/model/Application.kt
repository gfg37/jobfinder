package com.example.jobfinder.model

import jakarta.persistence.*

@Entity
@Table(name = "applications")
data class Application(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id")
    val resume: Resume,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vacancy_id")
    val vacancy: Vacancy,

    @Enumerated(EnumType.STRING)
    val status: ApplicationStatus = ApplicationStatus.PENDING
)

enum class ApplicationStatus {
    PENDING, ACCEPTED, REJECTED
}
