package com.example.jobfinder.service

import com.example.jobfinder.dto.ResumeRequest
import com.example.jobfinder.dto.ResumeResponse
import com.example.jobfinder.model.Resume
import com.example.jobfinder.model.User
import com.example.jobfinder.repository.ResumeRepository
import org.springframework.stereotype.Service

@Service
class ResumeService(
    private val resumeRepository: ResumeRepository
) {

    fun createResume(user: User, request: ResumeRequest): Resume {
        val resume = Resume(
            position = request.position,
            experience = request.experience,
            education = request.education,
            skills = request.skills,
            user = user
        )
        return resumeRepository.save(resume)
    }

    fun getAllResumes(): List<ResumeResponse> =
        resumeRepository.findAll().map { mapToResponse(it) }

    fun getUserResumes(user: User): List<ResumeResponse> =
        resumeRepository.findAllByUserId(user.id).map { mapToResponse(it) }



    fun mapToResponse(resume: Resume): ResumeResponse {
        return ResumeResponse(
            id = resume.id,
            position = resume.position,
            experience = resume.experience,
            education = resume.education,
            skills = resume.skills,
            userName = resume.user.name
        )
    }
}
