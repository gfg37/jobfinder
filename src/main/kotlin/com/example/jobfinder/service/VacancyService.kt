package com.example.jobfinder.service

import com.example.jobfinder.dto.VacancyRequest
import com.example.jobfinder.dto.VacancyResponse
import com.example.jobfinder.dto.VacancyUpdateRequest
import com.example.jobfinder.model.User
import com.example.jobfinder.model.Vacancy
import com.example.jobfinder.repository.VacancyRepository

import org.springframework.stereotype.Service

@Service
class VacancyService(
    private val vacancyRepository: VacancyRepository
) {

    fun createVacancy(user: User, request: VacancyRequest): Vacancy {
        val vacancy = Vacancy(
            title = request.title,
            description = request.description,
            requirements = request.requirements,
            salary = request.salary,
            employer = user
        )
        return vacancyRepository.save(vacancy)
    }

    fun getAll(): List<Vacancy> = vacancyRepository.findAll()

    fun getEmployerVacancies(user: User): List<Vacancy> =
        vacancyRepository.findAllByEmployerId(user.id)

    fun updateVacancy(user: User, id: Long, request: VacancyUpdateRequest): Vacancy {
        val vacancy = vacancyRepository.findById(id).orElseThrow()
        if (vacancy.employer.id != user.id) {
            throw IllegalAccessException("You are not allowed to update this vacancy")
        }

        val updated = vacancy.copy(
            title = request.title,
            description = request.description,
            salary = request.salary.toString()
        )

        return vacancyRepository.save(updated)
    }

    fun deleteVacancy(user: User, vacancyId: Long) {
        val vacancy = vacancyRepository.findById(vacancyId)
            .orElseThrow { IllegalArgumentException("Вакансия не найдена") }

        if (vacancy.employer.id != user.id) {
            throw IllegalAccessException("Вы не можете удалить чужую вакансию")
        }

        vacancyRepository.delete(vacancy)
    }





    fun mapToResponse(vacancy: Vacancy): VacancyResponse {
        return VacancyResponse(
            id = vacancy.id,
            title = vacancy.title,
            description = vacancy.description,
            requirements = vacancy.requirements,
            salary = vacancy.salary,
            employerName = vacancy.employer.name
        )
    }
}
