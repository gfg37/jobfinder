package com.example.jobfinder.controller

import com.example.jobfinder.model.Role
import com.example.jobfinder.model.User
import com.example.jobfinder.dto.ResumeResponse
import com.example.jobfinder.dto.VacancyResponse
import com.example.jobfinder.service.FavoriteService
import com.example.jobfinder.service.ResumeService
import com.example.jobfinder.service.VacancyService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/favorites")
class FavoriteController(
    private val favoriteService: FavoriteService,
    private val resumeService: ResumeService,
    private val vacancyService: VacancyService
) {

    // === VACANCIES ===

    @PostMapping("/vacancies/{id}")
    fun addVacancy(@AuthenticationPrincipal user: User, @PathVariable id: Long): ResponseEntity<Void> {
        if (user.role != Role.APPLICANT) return ResponseEntity.status(403).build()
        favoriteService.addVacancy(user, id)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/vacancies/{id}")
    fun removeVacancy(@AuthenticationPrincipal user: User, @PathVariable id: Long): ResponseEntity<Void> {
        if (user.role != Role.APPLICANT) return ResponseEntity.status(403).build()
        favoriteService.removeVacancy(user, id)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/vacancies")
    fun getFavoriteVacancies(@AuthenticationPrincipal user: User): ResponseEntity<List<VacancyResponse>> {
        if (user.role != Role.APPLICANT) return ResponseEntity.status(403).build()
        val list = favoriteService.getFavoriteVacancies(user).map { vacancyService.mapToResponse(it) }
        return ResponseEntity.ok(list)
    }

    // === RESUMES ===

    @PostMapping("/resumes/{id}")
    fun addResume(@AuthenticationPrincipal user: User, @PathVariable id: Long): ResponseEntity<Void> {
        if (user.role != Role.EMPLOYER) return ResponseEntity.status(403).build()
        favoriteService.addResume(user, id)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/resumes/{id}")
    fun removeResume(@AuthenticationPrincipal user: User, @PathVariable id: Long): ResponseEntity<Void> {
        if (user.role != Role.EMPLOYER) return ResponseEntity.status(403).build()
        favoriteService.removeResume(user, id)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/resumes")
    fun getFavoriteResumes(@AuthenticationPrincipal user: User): ResponseEntity<List<ResumeResponse>> {
        if (user.role != Role.EMPLOYER) return ResponseEntity.status(403).build()
        val list = favoriteService.getFavoriteResumes(user).map { resumeService.mapToResponse(it) }
        return ResponseEntity.ok(list)
    }
}
