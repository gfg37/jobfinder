package com.example.jobfinder.repository

import com.example.jobfinder.model.Application
import org.springframework.data.jpa.repository.JpaRepository

interface ApplicationRepository : JpaRepository<Application, Long> {
    fun findAllByVacancyEmployerId(employerId: Long): List<Application>
    fun findAllByResumeUserId(applicantId: Long): List<Application>
}
