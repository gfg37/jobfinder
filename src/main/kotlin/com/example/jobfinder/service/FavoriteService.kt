package com.example.jobfinder.service

import com.example.jobfinder.model.Resume
import com.example.jobfinder.model.User
import com.example.jobfinder.model.Vacancy
import com.example.jobfinder.repository.ResumeRepository
import com.example.jobfinder.repository.UserRepository
import com.example.jobfinder.repository.VacancyRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FavoriteService(
    private val userRepository: UserRepository,
    private val resumeRepository: ResumeRepository,
    private val vacancyRepository: VacancyRepository
) {

    @Transactional
    fun addVacancy(user: User, vacancyId: Long) {
        val managedUser = userRepository.findById(user.id).orElseThrow()
        val vacancy = vacancyRepository.findById(vacancyId)
            .orElseThrow { IllegalArgumentException("Вакансия не найдена") }

        managedUser.favoriteVacancies.add(vacancy)
        userRepository.save(managedUser)
    }

    @Transactional
    fun removeVacancy(user: User, vacancyId: Long) {
        val managedUser = userRepository.findById(user.id).orElseThrow()
        managedUser.favoriteVacancies.removeIf { it.id == vacancyId }
        userRepository.save(managedUser)
    }

    fun getFavoriteVacancies(user: User): Set<Vacancy> {
        val managedUser = userRepository.findById(user.id).orElseThrow()
        return managedUser.favoriteVacancies
    }

    @Transactional
    fun addResume(user: User, resumeId: Long) {
        val managedUser = userRepository.findById(user.id).orElseThrow()
        val resume = resumeRepository.findById(resumeId)
            .orElseThrow { IllegalArgumentException("Резюме не найдено") }

        managedUser.favoriteResumes.add(resume)
        userRepository.save(managedUser)
    }

    @Transactional
    fun removeResume(user: User, resumeId: Long) {
        val managedUser = userRepository.findById(user.id).orElseThrow()
        managedUser.favoriteResumes.removeIf { it.id == resumeId }
        userRepository.save(managedUser)
    }

    fun getFavoriteResumes(user: User): Set<Resume> {
        val managedUser = userRepository.findById(user.id).orElseThrow()
        return managedUser.favoriteResumes
    }
}
