package com.bfh.moduletracker.ai.service.auth;


import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);


    public String generateJwtToken(Authentication authentication) {
        UserDetails userPricipal = (UserDetails) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(userPricipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public long getExpirationTime() {
        return this.jwtExpiration;
    }

    public String extractUsername(String token) {
        return (String) extractClaim(token, "sub");
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String subject = (String) extractClaim(token, "sub");
        Long expirationSeconds = (Long) extractClaim(token, "exp");

        Date expiration = new Date(expirationSeconds * 1000);
        return (subject.equals(userDetails.getUsername())) && expiration.after(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Object extractClaim(String token, String claimKey) {
        return extractAllClaims(token).get(claimKey);
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
