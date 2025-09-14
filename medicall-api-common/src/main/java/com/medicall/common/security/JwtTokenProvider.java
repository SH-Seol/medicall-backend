package com.medicall.common.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.medicall.common.security.error.AuthErrorType;
import com.medicall.common.security.error.AuthException;

@Component
public class JwtTokenProvider {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.access-token-expiration}")
    private Long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private Long refreshTokenExpiration;

    public String generateAccessToken(Long userId, String serviceType){
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(userId.toString())
                .claim("serviceType", serviceType)
                .claim("type", "access")
                .issuedAt(new Date())
                .notBefore(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpiration * 1000))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public String generateRefreshToken(Long userId){
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(userId.toString())
                .claim("type", "refresh")
                .issuedAt(new Date())
                .notBefore(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpiration * 1000))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public boolean validateToken(String token) {
        try{
            Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parseSignedClaims(token);
            return true;
        }
        catch (JwtException e){
            return false;
        }
    }

    public Claims getClaimsFromToken(String token) {
        try{
            return Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        }catch (ExpiredJwtException e){
            throw new AuthException(AuthErrorType.EXPIRED_TOKEN);
        }catch (SignatureException e){
            throw new AuthException(AuthErrorType.INVALID_SIGNATURE);
        }catch (MalformedJwtException e){
            throw new AuthException(AuthErrorType.MALFORMED_TOKEN);
        } catch (JwtException e){
            throw new AuthException(AuthErrorType.INVALID_TOKEN);
        }
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        String subject = claims.getSubject();
        if(subject == null){
            throw new AuthException(AuthErrorType.INVALID_TOKEN);
        }
        try{
            return Long.valueOf(claims.getSubject());
        }catch (NumberFormatException e){
            throw new AuthException(AuthErrorType.INVALID_TOKEN);
        }
    }

    public String getServiceTypeFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        String serviceType = claims.get("serviceType", String.class);
        if (serviceType == null) {
            throw new AuthException(AuthErrorType.INVALID_TOKEN);
        }
        return serviceType;
    }
}
