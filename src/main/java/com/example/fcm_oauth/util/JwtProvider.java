package com.example.fcm_oauth.util;

import com.example.fcm_oauth.dto.TokenInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtProvider {


    private final Key key;
    @Value("${jwt.access-token.expire-length}")
    private Long accessTokenExpiresTime;
    @Value("${jwt.refresh-token.expire-length}")
    private Long refreshTokenExpiresTime;

    public JwtProvider(@Value("${jwt.key}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    private static String getAuthorities(Authentication authentication) {

        return authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    // Jwt 를 생성하는 메소드(Authentication)
    // 을 받아 AccessToken + RefreshToken 을 가지는 TokenInfo 를 반환
    public TokenInfo generateToken(Authentication authentication) {

        Long now = new Date().getTime();

        String accessToken = generateAccessToken(authentication);

        String refreshToken = generateRefreshToken(authentication);

        return TokenInfo.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

    }

    public String generateAccessToken(Authentication authentication) {
        String authorities = getAuthorities(authentication);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .setExpiration(new Date(new Date().getTime() + accessTokenExpiresTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Authentication authentication) {
        String authorities = getAuthorities(authentication);

        return Jwts.builder()
                .setExpiration(new Date(new Date().getTime() + refreshTokenExpiresTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

    }

    public Boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.error("error occurred!");
        }

        return null;
    }

    public Claims paresClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
