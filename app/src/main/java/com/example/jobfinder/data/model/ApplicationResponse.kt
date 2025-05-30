package com.example.jobfinder.data.model

data class ApplicationResponse(
    val id: Long,
    val resumeId: Long,
    val resumeOwner: String,
    val vacancyId: Long,
    val vacancyTitle: String,
    val employer: String,
    val status: String // "PENDING", "ACCEPTED", "REJECTED"
)