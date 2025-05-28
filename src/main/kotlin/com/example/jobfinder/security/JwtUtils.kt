package com.example.jobfinder.security

import com.example.jobfinder.model.User
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtUtils {

    private val jwtSecret = Keys.secretKeyFor(SignatureAlgorithm.HS256)
    private val expirationMs = 86400000 // 1 день

    fun generateToken(user: User): String {
        return Jwts.builder()
            .setSubject(user.email)
            .claim("role", user.role.name)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + expirationMs))
            .signWith(jwtSecret)
            .compact()
    }

    fun extractUsername(token: String): String =
        Jwts.parserBuilder().setSigningKey(jwtSecret).build()
            .parseClaimsJws(token).body.subject
}
