package com.example.jobfinder.controller

import com.example.jobfinder.dto.VacancyRequest
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
    fun getAll(): ResponseEntity<List<Vacancy>> {

        return ResponseEntity.ok(vacancyService.getAll())
    }



}
