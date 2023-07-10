package com.example.fcm_oauth.dto;

import com.example.fcm_oauth.domain.entity.Users;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

/*
  Kakao api 로 부터 얻어오는 json 정보
*/
@Getter
public class KakaoAttributes {

    private final String nameAttributeKey;
    private final OAuthUserInfo oauthUserInfo;

    @Builder
    public KakaoAttributes(String nameAttributeKey, OAuthUserInfo oauthUserInfo) {
        this.nameAttributeKey = nameAttributeKey;
        this.oauthUserInfo = oauthUserInfo;
    }

    /*
         api 로 받아온 유저 정보인 attributes key nameAttributeKey 와 attributes 를 받아서 객체 생성
    */
    public static KakaoAttributes of(String nameAttributeKey, Map<String, Object> attributes) {
        return KakaoAttributes.builder()
                .nameAttributeKey(nameAttributeKey)
                .oauthUserInfo(new KakaoUserInfo(attributes))
                .build();
    }

    public Users toEntity(OAuthUserInfo oauthUserInfo) {
        return Users.builder()
                .oauthId(oauthUserInfo.getOauthId())
                .nickName(oauthUserInfo.getNickName())
                .imageUrl(oauthUserInfo.getImageUrl())
                .role(Role.USER)
                .build();
    }

}
