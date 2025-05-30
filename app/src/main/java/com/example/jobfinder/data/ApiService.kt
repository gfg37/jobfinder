package com.example.jobfinder.data


import com.example.jobfinder.data.model.ApplicationRequest
import com.example.jobfinder.data.model.ApplicationResponse
import com.example.jobfinder.data.model.AuthResponse
import com.example.jobfinder.data.model.LoginRequest
import com.example.jobfinder.data.model.ResumeRequest
import com.example.jobfinder.data.model.ResumeResponse
import com.example.jobfinder.data.model.UpdateApplicationStatusRequest
import com.example.jobfinder.data.model.VacancyRequest
import com.example.jobfinder.data.model.VacancyResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path


// ApiService.kt
interface ApiService {
    // Аутентификация
    @POST("/api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    // Резюме
    @GET("/api/resumes")
    suspend fun getAllResumes(@Header("Authorization") token: String): Response<List<ResumeResponse>>

    @GET("/api/resumes/my")
    suspend fun getMyResumes(@Header("Authorization") token: String): Response<List<ResumeResponse>>

    @POST("/api/resumes")
    suspend fun createResume(
        @Header("Authorization") token: String,
        @Body request: ResumeRequest
    ): Response<ResumeResponse>

    // Вакансии
    @GET("/api/vacancies")
    suspend fun getAllVacancies(@Header("Authorization") token: String): Response<List<VacancyResponse>>

    @GET("/api/vacancies/my")
    suspend fun getMyVacancies(@Header("Authorization") token: String): Response<List<VacancyResponse>>

    @POST("/api/vacancies")
    suspend fun createVacancy(
        @Header("Authorization") token: String,
        @Body request: VacancyRequest
    ): Response<VacancyResponse>



    @DELETE("/api/resumes/{id}")
    suspend fun deleteResume(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Response<Unit>

    // Удаление вакансии
    @DELETE("/api/vacancies/{id}")
    suspend fun deleteVacancy(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Response<Unit>

    // Отклик на вакансию
    @POST("/api/applications")
    suspend fun applyForVacancy(
        @Header("Authorization") token: String,
        @Body request: ApplicationRequest
    ): Response<ApplicationResponse>

    // Получение своих откликов (для соискателя)
    @GET("/api/applications/my")
    suspend fun getMyApplications(
        @Header("Authorization") token: String
    ): Response<List<ApplicationResponse>>

    // Получение откликов на вакансии (для работодателя)
    @GET("/api/applications/employer")
    suspend fun getEmployerApplications(
        @Header("Authorization") token: String
    ): Response<List<ApplicationResponse>>

    // Обновление статуса отклика
    @PATCH("/api/applications/{id}/status")
    suspend fun updateApplicationStatus(
        @Header("Authorization") token: String,
        @Path("id") id: Long,
        @Body request: UpdateApplicationStatusRequest
    ): Response<ApplicationResponse>

    // Добавление вакансии в избранное
    @POST("/api/favorites/vacancies/{id}")
    suspend fun addVacancyToFavorites(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Response<Unit>

    // Удаление вакансии из избранного
    @DELETE("/api/favorites/vacancies/{id}")
    suspend fun removeVacancyFromFavorites(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Response<Unit>

    // Добавление резюме в избранное
    @POST("/api/favorites/resumes/{id}")
    suspend fun addResumeToFavorites(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Response<Unit>

    // Удаление резюме из избранного
    @DELETE("/api/favorites/resumes/{id}")
    suspend fun removeResumeFromFavorites(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Response<Unit>

    // Получение избранных вакансий
    @GET("/api/favorites/vacancies")
    suspend fun getFavoriteVacancies(
        @Header("Authorization") token: String
    ): Response<List<VacancyResponse>>

    // Получение избранных резюме
    @GET("/api/favorites/resumes")
    suspend fun getFavoriteResumes(
        @Header("Authorization") token: String
    ): Response<List<ResumeResponse>>
}