package com.example.jobfinder.repository

import com.example.jobfinder.model.Vacancy
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface VacancyRepository : JpaRepository<Vacancy, Long> {
    fun findAllByEmployerId(employerId: Long): List<Vacancy>

}
