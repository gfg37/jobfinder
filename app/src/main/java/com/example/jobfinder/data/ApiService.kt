package com.example.jobfinder.data


import com.example.jobfinder.data.model.AuthResponse
import com.example.jobfinder.data.model.LoginRequest
import com.example.jobfinder.data.model.ResumeRequest
import com.example.jobfinder.data.model.ResumeResponse
import com.example.jobfinder.data.model.UserProfileResponse
import com.example.jobfinder.data.model.VacancyRequest
import com.example.jobfinder.data.model.VacancyResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST


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



}