package com.example.fcm_oauth.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.fcm_oauth.domain.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Getter
@RequiredArgsConstructor
@Service
public class JwtService {

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String EMAIL_CLAIM = "email";
    private static final String BEARER = "Bearer ";
    /**
     * JWT subject & claim 으로 Email 사용
     * JWT 헤더에 들어오는 값 : 'Authorization(key) = Bearer 토큰(value) 형식
     */

    private final UserRepository userRepository;
    @Value("${jwt.key}")
    private String secretKey;
    @Value("${jwt.access.expiration}")
    private String accessTokenExpiration;
    @Value("${jwt.refresh.expiration}")
    private String refreshTokenExpiration;
    @Value("${jwt.access.header}")
    private String accessTokenHeader;
    @Value("${jwt.refresh.header}")
    private String refreshTokenHeader;

    private String createAccessToken(String email) {
        Long now = new Date().getTime();
        Date accessTokenExpiresIn = new Date(now + accessTokenExpiration);

        return JWT.create()
                .withSubject(ACCESS_TOKEN_SUBJECT)
                .withExpiresAt(accessTokenExpiresIn)
                .withClaim(EMAIL_CLAIM, email) // claim 으로 email 만 사용
                .sign(Algorithm.HMAC512(secretKey)); // HMAC512 알고리즘 사용
    }

    private String createRefreshToken(String email) {
        Long now = new Date().getTime();
        Date refreshTokenExpiresIn = new Date(now + refreshTokenExpiration);

        return JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                // claim 필요 x
                .withExpiresAt(refreshTokenExpiresIn)
                .sign(Algorithm.HMAC512(secretKey));
    }

    /**
     * access token 헤더에 실어 보내기
     */
    private void sendAccessToken(HttpServletResponse response, String accessToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader(accessTokenHeader, accessToken);
        log.info("재발급된 access token : {}", accessToken);
    }

    /**
     * access token + refresh token 헤더에 실어 보내기
     */
    private void sendAccessTokenAndRefreshToken(HttpServletResponse response,
                                                String accessToken,
                                                String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader(accessTokenHeader, accessToken);
        response.setHeader(refreshTokenHeader, refreshToken);
        log.info("access token, refresh token 발급");
    }

    /**
     * 요청 헤더에서 Refresh Token 추출
     * 토큰 형식 : Bearer {token} 에서 Bearer 제외
     * 헤더를 가져온 후 Bearer 삭제 ("" 로 대체)
     */
    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(refreshTokenHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));

    }

    /**
     * 요청 헤더에서 Access Token 추출
     * 토큰 형식 : Bearer {token} 에서 Bearer 제외
     * 헤더를 가져온 후 Bearer 삭제 ("" 로 대체)
     */
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(accessTokenHeader))
                .filter(accessToken -> accessToken.startsWith(BEARER))
                .map(accessToken -> accessToken.replace(BEARER, ""));
    }

    /*
        AcessToken 으로부터 email 정보 가져오는 메소드
    */
    public Optional<String> extractEmail(String accessToken) {
        try {
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                    .build()
                    .verify(accessToken)
                    .getClaim(EMAIL_CLAIM)
                    .asString());

        } catch (Exception e) {
            log.error("엑시스 토큰이 유효하지 않습니다.");
            return Optional.empty();
        }
    }


}
