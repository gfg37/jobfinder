package com.example.jobfinder.controller

import com.example.jobfinder.dto.ResumeRequest
import com.example.jobfinder.dto.ResumeResponse
import com.example.jobfinder.model.Resume
import com.example.jobfinder.model.Role
import com.example.jobfinder.model.User
import com.example.jobfinder.service.ResumeService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/resumes")
class ResumeController(
    private val resumeService: ResumeService
) {

    @PostMapping
    fun createResume(
        @AuthenticationPrincipal user: User,
        @RequestBody request: ResumeRequest
    ): ResponseEntity<Resume> {
        if (user.role != Role.APPLICANT) {
            return ResponseEntity.status(403).build()
        }
        val resume = resumeService.createResume(user, request)
        return ResponseEntity.ok(resume)
    }

    @GetMapping("/my")
    fun getMyResumes(@AuthenticationPrincipal user: User): ResponseEntity<List<ResumeResponse>> {
        if (user.role != Role.APPLICANT) {
            return ResponseEntity.status(403).build()
        }
        return ResponseEntity.ok(resumeService.getUserResumes(user))
    }

    @GetMapping
    fun getAllResumes(@AuthenticationPrincipal user: User): ResponseEntity<List<ResumeResponse>> {
        if (user.role != Role.EMPLOYER) {
            return ResponseEntity.status(403).build()
        }
        return ResponseEntity.ok(resumeService.getAllResumes())
    }

}
