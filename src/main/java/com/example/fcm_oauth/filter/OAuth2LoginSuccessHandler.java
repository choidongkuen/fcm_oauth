package com.example.fcm_oauth.filter;

import com.example.fcm_oauth.domain.repository.UserRepository;
import com.example.fcm_oauth.dto.KakaoOAuthUser;
import com.example.fcm_oauth.dto.TokenInfo;
import com.example.fcm_oauth.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 login 성공");

        try {
            KakaoOAuthUser kakaoOAuthUser = (KakaoOAuthUser) authentication.getPrincipal();
            TokenInfo tokenInfo = jwtProvider.generateToken(authentication);

        } catch (Exception e) {

        }
    }
}
