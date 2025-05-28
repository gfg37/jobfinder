package com.example.jobfinder.repository

import com.example.jobfinder.model.Role
import com.example.jobfinder.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?
    fun existsByEmail(email: String): Boolean
    fun findByIdAndRole(id: Long, role: Role): User?

}
