package com.example.jobfinder.controller

import com.example.jobfinder.dto.VacancyRequest
import com.example.jobfinder.dto.VacancyResponse
import com.example.jobfinder.dto.VacancyUpdateRequest
import com.example.jobfinder.model.Role
import com.example.jobfinder.model.User
import com.example.jobfinder.model.Vacancy
import com.example.jobfinder.service.VacancyService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/vacancies")
class VacancyController(
    private val vacancyService: VacancyService
) {

    @PostMapping
    fun createVacancy(
        @AuthenticationPrincipal user: User,
        @RequestBody request: VacancyRequest
    ): ResponseEntity<Vacancy> {
        if (user.role != Role.EMPLOYER) {
            return ResponseEntity.status(403).build()
        }
        val vacancy = vacancyService.createVacancy(user, request)
        return ResponseEntity.ok(vacancy)
    }

    @GetMapping("/my")
    fun getMyVacancies(@AuthenticationPrincipal user: User): ResponseEntity<List<Vacancy>> {
        println("Пользователь: ${user.email}, роль: ${user.role}")
        if (user.role != Role.EMPLOYER) {
            return ResponseEntity.status(403).build()
        }
        return ResponseEntity.ok(vacancyService.getEmployerVacancies(user))
    }

    @GetMapping
    fun getAllVacancies(@AuthenticationPrincipal user: User): ResponseEntity<List<VacancyResponse>> {
        if (user.role != Role.APPLICANT) {
            return ResponseEntity.status(403).build()
        }
        return ResponseEntity.ok(vacancyService.getAllVacancies())
    }


    @PutMapping("/{id}")
    fun updateVacancy(
        @AuthenticationPrincipal user: User,
        @PathVariable id: Long,
        @RequestBody request: VacancyUpdateRequest
    ): ResponseEntity<VacancyResponse> {
        if (user.role != Role.EMPLOYER) {
            return ResponseEntity.status(403).build()
        }
        val updated = vacancyService.updateVacancy(user, id, request)
        return ResponseEntity.ok(vacancyService.mapToResponse(updated))
    }


    @DeleteMapping("/{id}")
    fun deleteVacancy(
        @AuthenticationPrincipal user: User,
        @PathVariable id: Long
    ): ResponseEntity<Void> {
        return try {
            vacancyService.deleteVacancy(user, id)
            ResponseEntity.noContent().build()
        } catch (e: IllegalAccessException) {
            ResponseEntity.status(403).build()
        } catch (e: IllegalArgumentException) {
            ResponseEntity.notFound().build()
        }
    }






}
