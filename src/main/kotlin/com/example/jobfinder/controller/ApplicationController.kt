package com.example.jobfinder.controller

import com.example.jobfinder.dto.ApplicationRequest
import com.example.jobfinder.dto.ApplicationResponse
import com.example.jobfinder.dto.UpdateApplicationStatusRequest
import com.example.jobfinder.model.Role
import com.example.jobfinder.model.User
import com.example.jobfinder.service.ApplicationService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/applications")
class ApplicationController(
    private val applicationService: ApplicationService
) {

    @PostMapping
    fun apply(
        @AuthenticationPrincipal user: User,
        @RequestBody request: ApplicationRequest
    ): ResponseEntity<ApplicationResponse> {
        if (user.role != Role.APPLICANT) return ResponseEntity.status(403).build()
        return ResponseEntity.ok(applicationService.apply(user, request))
    }

    @GetMapping("/my")
    fun myApplications(@AuthenticationPrincipal user: User): ResponseEntity<List<ApplicationResponse>> {
        if (user.role != Role.APPLICANT) return ResponseEntity.status(403).build()
        return ResponseEntity.ok(applicationService.getMyApplications(user))
    }

    @GetMapping("/employer")
    fun employerApplications(@AuthenticationPrincipal user: User): ResponseEntity<List<ApplicationResponse>> {
        if (user.role != Role.EMPLOYER) return ResponseEntity.status(403).build()
        return ResponseEntity.ok(applicationService.getEmployerApplications(user))
    }

    @PatchMapping("/{id}/status")
    fun updateStatus(
        @AuthenticationPrincipal user: User,
        @PathVariable id: Long,
        @RequestBody request: UpdateApplicationStatusRequest
    ): ResponseEntity<ApplicationResponse> {
        if (user.role != Role.EMPLOYER) return ResponseEntity.status(403).build()
        return try {
            ResponseEntity.ok(applicationService.updateStatus(id, user, request.status))
        } catch (ex: Exception) {
            ResponseEntity.badRequest().body(null)
        }
    }

}
