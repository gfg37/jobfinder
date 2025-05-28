package com.example.jobfinder.dto

import com.example.jobfinder.model.ApplicationStatus

data class UpdateApplicationStatusRequest(
    val status: ApplicationStatus
)
