package com.example.training.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j
public class JwtTokenProvider {

    @Value("${app.security.jwt.secret}")
    private String jwtSecret;

    @Value("${app.security.jwt.expiration}")
    private long jwtExpiration;

    private String secretKey;

    @PostConstruct
    protected void init() {
        try {
            // Ensure the secret is at least minimally encoded
            secretKey = Base64.getEncoder().encodeToString(jwtSecret.getBytes());
            log.debug("JWT secret key initialized successfully");
        } catch (Exception e) {
            log.error("Failed to initialize JWT secret key", e);
            throw new RuntimeException("Failed to initialize JWT secret key", e);
        }
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        try {
            log.debug("Generating JWT token for user: {}", userDetails.getUsername());
            String token = Jwts.builder()
                    .setClaims(extraClaims)
                    .setSubject(userDetails.getUsername())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                    .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                    .compact();
            log.debug("JWT token generated successfully");
            return token;
        } catch (Exception e) {
            log.error("Error generating JWT token for user: {}", userDetails.getUsername(), e);
            throw new JwtException("Failed to generate JWT token", e);
        }
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            log.debug("Validating JWT token for user: {}", userDetails.getUsername());
            final String username = extractUsername(token);
            boolean isValid = (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
            log.debug("JWT token validation result: {}", isValid);
            return isValid;
        } catch (Exception e) {
            log.error("Error validating JWT token for user: {}", userDetails.getUsername(), e);
            return false;
        }
    }

    public String extractUsername(String token) {
        try {
            return extractClaim(token, Claims::getSubject);
        } catch (Exception e) {
            log.error("Error extracting username from token", e);
            throw new JwtException("Failed to extract username from token", e);
        }
    }

    public Date extractExpiration(String token) {
        try {
            return extractClaim(token, Claims::getExpiration);
        } catch (Exception e) {
            log.error("Error extracting expiration from token", e);
            throw new JwtException("Failed to extract expiration from token", e);
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        try {
            final Claims claims = extractAllClaims(token);
            return claimsResolver.apply(claims);
        } catch (Exception e) {
            log.error("Error extracting claim from token", e);
            throw new JwtException("Failed to extract claim from token", e);
        }
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("Error extracting all claims from token", e);
            throw new JwtException("Failed to extract all claims from token", e);
        }
    }

    private Key getSigningKey() {
        try {
            // Use the properly encoded secret key
            byte[] keyBytes = Decoders.BASE64.decode(secretKey);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            log.error("Error getting signing key", e);
            throw new JwtException("Failed to get signing key", e);
        }
    }

    private boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (Exception e) {
            log.error("Error checking if token is expired", e);
            return true; // Consider expired if there's an error
        }
    }
}
