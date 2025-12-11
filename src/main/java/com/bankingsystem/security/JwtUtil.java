
package com.bankingsystem.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class JwtUtil {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private final long ACCESS_TOKEN_VALIDITY = 1000 * 60 * 15; // 15 minutes
    private final long REFRESH_TOKEN_VALIDITY = 1000 * 60 * 60 * 24; // 24 hours

    // ✅ Generate Access Token with role and userId
    public String generateAccessToken(String username, String role, Long userId, boolean active) {
        return Jwts.builder()
                .setSubject(username)
                .claim("username",username)
                .claim("role", role)
                .claim("userId", userId)
                .claim("active", active) // ✅ Add active status
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY))
                .signWith(key)
                .compact();
    }

    // ✅ Generate Refresh Token (only username)
    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY))
                .signWith(key)
                .compact();
    }

    // ✅ Extract username from token
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
