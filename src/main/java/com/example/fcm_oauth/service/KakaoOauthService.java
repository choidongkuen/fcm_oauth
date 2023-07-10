package com.example.fcm_oauth.service;

import com.example.fcm_oauth.domain.repository.MemberRepository;
import com.example.fcm_oauth.dto.KakaoUserInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoOauthService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    public KakaoUserInfo kakaoLogin(String code, HttpServletResponse httpServletResponse) throws JsonProcessingException {
        return null; // TODO
    }
}
