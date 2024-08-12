package com.codecraftwithkevin.security_jwt.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    @Value("${token.secret}")
    private String secret;
    public String extractSubject(String token) {
        return extractClaims(token).getSubject();
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(generateSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    private SecretKey generateSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }


    public boolean isTokenValid(String jwtTokenHeader) {
        return new Date().before(extractExpiration(jwtTokenHeader));
    }

    private Date extractExpiration(String jwtTokenHeader) {
        return extractClaims(jwtTokenHeader).getExpiration();
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .header()
                .type("JWT")
                .and()
                .subject(username)
                .issuedAt(getIssuedAt())
                .expiration(getExpiration())
                .signWith(generateSigningKey())
                .compact();
    }


    private Date getExpiration() {
        Date expirationDate = new Date(System.currentTimeMillis() +(1000 * 24*24)); // testing purpose
        return expirationDate;
    }

    private Date getIssuedAt() {
        Date issueAt = new Date(System.currentTimeMillis());
        return issueAt;
    }
}
