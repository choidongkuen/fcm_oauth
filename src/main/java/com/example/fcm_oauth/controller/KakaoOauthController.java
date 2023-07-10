package com.example.fcm_oauth.controller;

import com.example.fcm_oauth.dto.KakaoUserInfo;
import com.example.fcm_oauth.service.KakaoOauthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RequiredArgsConstructor
@RestController
public class KakaoOauthController {

    private final KakaoOauthService kakaoOauthService;

//    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
//    private String clientId;

    @GetMapping("/login/callback/kakao")
    public KakaoUserInfo kakaoLogin(
            @RequestParam(value = "code") String code,
            HttpServletResponse httpServletResponse
    ) throws JsonProcessingException {
        return null;
    }
}

// https://kauth.kakao.com/oauth/authorize?client_id=edb8647a8a046e4f9c835ca0036fba22&redirect_uri=http://localhost:8080/login/callback/kakao&response_type=code