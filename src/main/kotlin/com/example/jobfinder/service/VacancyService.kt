package com.example.jobfinder.service

import com.example.jobfinder.dto.VacancyRequest
import com.example.jobfinder.dto.VacancyResponse
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
