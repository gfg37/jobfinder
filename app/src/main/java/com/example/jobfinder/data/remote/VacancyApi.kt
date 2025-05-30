
package com.example.jobfinder.data.remote

import com.example.jobfinder.data.model.VacancyRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface VacancyApi {
    @POST("/api/vacancies")
    suspend fun createVacancy(
        @Body request: VacancyRequest,
        @Header("Authorization") token: String
    ): Response<Unit>
}
