package com.example.fcm_oauth.service;

import com.example.fcm_oauth.domain.entity.Users;
import com.example.fcm_oauth.domain.repository.UserRepository;
import com.example.fcm_oauth.dto.KakaoAttributes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

// 인가 코드 -> redirect url -> login/callback/kakao
@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoOauthService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info("KakaoOauthService.loadUser() 실행 - Oauth2 로그인 요청 진입");

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();

        // kakao 인증 서버로 사용자 정보 요청을 보냄
        // Oauth2User 는 authorities,attributes,nameAttributeKey 정보를 가짐
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // 소셜 로그인에서 API 제공하는 user info json data
        Map<String, Object> attributes = oAuth2User.getAttributes();

        KakaoAttributes kakaoAttributes = KakaoAttributes.of(userNameAttributeName, attributes);
        Users createdUser = getUser(kakaoAttributes);

        return null;
    }

    /*
        userRepository 에서 oauthId 로 회원 정보 get 없으면 새로 생성
    */
    private Users getUser(KakaoAttributes kakaoAttributes) {

        Users foundUser = this.userRepository.findByOauthId(
                        kakaoAttributes.getOauthUserInfo().getOauthId())
                .orElse(null);

        if (foundUser == null) {
            return saveUser(kakaoAttributes);
        }
        return foundUser;
    }

    /*
        userRepository 에 없는 경우 새로 저장 후 리턴
     */
    private Users saveUser(KakaoAttributes kakaoAttributes) {
        return this.userRepository.save(
                kakaoAttributes.toEntity(kakaoAttributes.getOauthUserInfo()));
    }


}
