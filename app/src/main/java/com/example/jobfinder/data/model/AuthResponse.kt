package com.example.jobfinder.data.model

import android.util.Base64
import org.json.JSONObject

data class AuthResponse(val token: String) {
    fun getRoleFromToken(): String? {
        return try {
            val parts = token.split(".")
            if (parts.size < 2) return null

            val payload = String(
                Base64.decode(parts[1], Base64.URL_SAFE or Base64.NO_PADDING),
                Charsets.UTF_8
            )

            // Пробуем разные варианты названий роли
            JSONObject(payload).getString("role")
                ?: JSONObject(payload).getString("authority")
                ?: JSONObject(payload).getString("user_role")
        } catch (e: Exception) {
            null
        }
    }
}