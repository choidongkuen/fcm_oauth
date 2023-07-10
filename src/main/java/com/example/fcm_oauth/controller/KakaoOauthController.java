package com.example.fcm_oauth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@RestController
public class KakaoOauthController {


    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @GetMapping("login/callback/kakao")
    public ResponseEntity<String> getAccessToken(
            @RequestParam(value = "code") String code
    ) {
        System.out.println("code = " + code);

        // 1. Header 설정
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");

        // 2. body 설정
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "edb8647a8a046e4f9c835ca0036fba22");
        params.add("redirect_uri", "http://localhost:8080/login/callback/kakao");

        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, httpHeaders);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Object> response = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                httpEntity,
                Object.class
        );

        System.out.println("response = " + response);
        return ResponseEntity.ok("hello");
    }
}

// https://kauth.kakao.com/oauth/authorize?client_id=edb8647a8a046e4f9c835ca0036fba22&redirect_uri=http://localhost:8080/login/callback/kakao&response_type=code