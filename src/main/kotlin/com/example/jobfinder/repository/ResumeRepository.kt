package com.example.jobfinder.repository

import com.example.jobfinder.model.Resume
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ResumeRepository : JpaRepository<Resume, Long> {
    fun findAllByUserId(userId: Long): List<Resume>
}
