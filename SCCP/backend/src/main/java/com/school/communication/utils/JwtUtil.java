package com.school.communication.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    
    @Value("${jwt.secret}")
    private String secret;
    
    @Value("${jwt.expiration}")
    private Long expiration;
    
    public String generateToken(Long userId, String username, String role) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expiration);
        
        return JWT.create()
                .withSubject(String.valueOf(userId))
                .withClaim("userId", userId)
                .withClaim("username", username)
                .withClaim("role", role)
                .withIssuedAt(now)
                .withExpiresAt(expireDate)
                .sign(Algorithm.HMAC256(secret));
    }
    
    public DecodedJWT verifyToken(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();
        return verifier.verify(token);
    }
    
    public Long getUserIdFromToken(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim("userId").asLong();
    }
    
    public String getUsernameFromToken(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim("username").asString();
    }
    
    public String getRoleFromToken(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim("role").asString();
    }
    
    public boolean isTokenExpired(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getExpiresAt().before(new Date());
    }
}