package com.example.jobfinder.controller

import com.example.jobfinder.dto.UserProfileResponse
import com.example.jobfinder.dto.UserProfileUpdateRequest
import com.example.jobfinder.model.User
import com.example.jobfinder.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/profile")
class UserController(
    private val userService: UserService
) {

    @GetMapping
    fun getProfile(@AuthenticationPrincipal user: User): ResponseEntity<UserProfileResponse> {
        return ResponseEntity.ok(userService.getProfile(user))
    }

    @PutMapping
    fun updateProfile(
        @AuthenticationPrincipal user: User,
        @RequestBody request: UserProfileUpdateRequest
    ): ResponseEntity<UserProfileResponse> {
        return ResponseEntity.ok(userService.updateProfile(user, request))
    }
}
