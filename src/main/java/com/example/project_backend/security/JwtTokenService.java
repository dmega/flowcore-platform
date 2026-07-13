package com.example.project_backend.security;

import com.example.project_backend.model.user.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final JwtProperties jwtProperties;
    private final JwtTokenHelper jwtTokenHelper;

    public String createToken(final UUID userId, final String username, Set<UserRole> roles) {
        Date now = new Date();
        Date expiration = Date.from(Instant.now().plus(jwtProperties.getExpiration()));
        return Jwts.builder()
                .claim("id", userId)
                .subject(username)
                .claim("roles", roles)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(jwtTokenHelper.getKey())
                .compact();
    }

    public String extractUsername(final String token) {
        return Jwts.parser()
                .verifyWith(jwtTokenHelper.getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean isTokenValid(final String token, final @NonNull UserDetails userDetails) {
        Claims claim = Jwts.parser()
                .verifyWith(jwtTokenHelper.getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String username = claim.getSubject();
        Date expiration = claim.getExpiration();

        return username.equals(userDetails.getUsername()) &&
                expiration.after(new Date());
    }

}
