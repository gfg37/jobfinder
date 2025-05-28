package com.example.jobfinder.dto

import com.example.jobfinder.model.ApplicationStatus

data class ApplicationResponse(
    val id: Long,
    val resumeId: Long,
    val resumeOwner: String,
    val vacancyId: Long,
    val vacancyTitle: String,
    val employer: String,
    val status: ApplicationStatus
)
