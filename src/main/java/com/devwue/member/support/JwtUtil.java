package com.devwue.member.support;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.util.Base64Utils;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
public class JwtUtil {
    private static final String JWT_SECRET = "member-service-test";
    private static final int JWT_EXPIRATION_1H = 1000 * 60 * 60;
    private static final String JWT_SECRET_ENCODED = Base64Utils.encodeToString(JWT_SECRET.getBytes(StandardCharsets.UTF_8));

    private JwtUtil() {}

    public static String newToken(Authentication authentication) {
        Date now = new Date(System.currentTimeMillis());
        return Jwts.builder()
                .setSubject(String.valueOf(authentication.getPrincipal()))
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + JWT_EXPIRATION_1H))
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET_ENCODED)
                .compact();
    }

    public static String parseToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET_ENCODED)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public static Boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET_ENCODED).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.error("JWT token validatie exception: {} {}", e.getCause(), e.getMessage(), e);
        }
        return false;
    }
}
