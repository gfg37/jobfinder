package com.example.jobfinder.model

import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = false)
    val password: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val role: Role,

    @ManyToMany
    @JoinTable(
        name = "favorite_vacancies",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "vacancy_id")]
    )
    val favoriteVacancies: MutableSet<Vacancy> = mutableSetOf(),

    @ManyToMany
    @JoinTable(
        name = "favorite_resumes",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "resume_id")]
    )
    val favoriteResumes: MutableSet<Resume> = mutableSetOf()
) {
    override fun toString(): String {
        return "User(id=$id, name='$name', email='$email', role=$role)"
    }
}


enum class Role {
    APPLICANT, EMPLOYER
}


