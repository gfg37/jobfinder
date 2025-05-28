package com.example.jobfinder.service

import com.example.jobfinder.dto.ApplicationRequest
import com.example.jobfinder.dto.ApplicationResponse
import com.example.jobfinder.model.Application
import com.example.jobfinder.model.ApplicationStatus
import com.example.jobfinder.model.User
import com.example.jobfinder.repository.ApplicationRepository
import com.example.jobfinder.repository.ResumeRepository
import com.example.jobfinder.repository.VacancyRepository
import org.springframework.stereotype.Service

@Service
class ApplicationService(
    private val applicationRepository: ApplicationRepository,
    private val resumeRepository: ResumeRepository,
    private val vacancyRepository: VacancyRepository
) {
    fun apply(user: User, request: ApplicationRequest): ApplicationResponse {
        val resume = resumeRepository.findById(request.resumeId)
            .orElseThrow { IllegalArgumentException("Резюме не найдено") }

        if (resume.user.id != user.id) {
            throw IllegalAccessException("Нельзя использовать чужое резюме")
        }

        val vacancy = vacancyRepository.findById(request.vacancyId)
            .orElseThrow { IllegalArgumentException("Вакансия не найдена") }

        val application = Application(
            resume = resume,
            vacancy = vacancy
        )
        return mapToResponse(applicationRepository.save(application))
    }

    fun getMyApplications(user: User): List<ApplicationResponse> =
        applicationRepository.findAllByResumeUserId(user.id)
            .map { mapToResponse(it) }

    fun getEmployerApplications(user: User): List<ApplicationResponse> =
        applicationRepository.findAllByVacancyEmployerId(user.id)
            .map { mapToResponse(it) }

    fun mapToResponse(app: Application): ApplicationResponse {
        return ApplicationResponse(
            id = app.id,
            resumeId = app.resume.id,
            resumeOwner = app.resume.user.name,
            vacancyId = app.vacancy.id,
            vacancyTitle = app.vacancy.title,
            employer = app.vacancy.employer.name,
            status = app.status
        )
    }

    fun updateStatus(applicationId: Long, employer: User, newStatus: ApplicationStatus): ApplicationResponse {
        val application = applicationRepository.findById(applicationId)
            .orElseThrow { IllegalArgumentException("Отклик не найден") }

        if (application.vacancy.employer.id != employer.id) {
            throw IllegalAccessException("Нельзя управлять откликами чужих вакансий")
        }

        val updated = application.copy(status = newStatus)
        return mapToResponse(applicationRepository.save(updated))
    }

}
