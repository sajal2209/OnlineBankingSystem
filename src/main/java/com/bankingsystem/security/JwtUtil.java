//
//package com.bankingsystem.security;
//
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
//import java.security.Key;
//import java.util.Date;
//
//import lombok.Data;
//import org.springframework.stereotype.Component;
//
//@Component
//@Data
//public class JwtUtil {
//
//    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
//    private final long ACCESS_TOKEN_VALIDITY = 1000 * 60 * 15; // 15 minutes
//    private final long REFRESH_TOKEN_VALIDITY = 1000 * 60 * 60 * 24; // 24 hours
//
//    // ✅ Generate Access Token with role and userId
//    public String generateAccessToken(String username, String role, Long userId, boolean active) {
//        return Jwts.builder()
//                .setSubject(username)
//                .claim("username",username)
//                .claim("role", role)
//                .claim("userId", userId)
//                .claim("active", active) // ✅ Add active status
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY))
//                .signWith(key)
//                .compact();
//    }
//
//    // ✅ Generate Refresh Token (only username)
//    public String generateRefreshToken(String username) {
//        return Jwts.builder()
//                .setSubject(username)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY))
//                .signWith(key)
//                .compact();
//    }
//
//    // ✅ Extract username from token
//    public String extractUsername(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJws(token)
//                .getBody()
//                .getSubject();
//    }
//}

package com.bankingsystem.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${security.jwt.secret}")
    private String secretBase64;

    @Value("${security.jwt.access-valid-ms:900000}")
    private long accessValidityMs;

    @Value("${security.jwt.refresh-valid-ms:86400000}")
    private long refreshValidityMs;

    private SecretKey key;

    @PostConstruct
    void init() {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretBase64));
    }

    public Claims parseClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody();
    }

    public boolean isValid(String token) {
        try { parseClaims(token); return true; } catch (Exception e) { return false; }
    }

    public String generateAccessToken(String username, String role, Long userId, boolean active) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)  // "ADMIN" | "BANKER" | "CUSTOMER"
                .claim("userId", userId)
                .claim("active", active)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessValidityMs))
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(String username, String role) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshValidityMs))
                .signWith(key)
                .compact();
    }
}