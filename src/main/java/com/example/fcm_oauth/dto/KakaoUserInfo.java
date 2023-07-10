package com.example.fcm_oauth.dto;

import java.util.Map;

public class KakaoUserInfo extends OAuthUserInfo {

    public KakaoUserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    private static Map<String, Object> getProfile(Map<String, Object> account, String profile) {
        return (Map<String, Object>) account.get(profile);
    }

    @Override
    public String getId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getNickName() {

        Map<String, Object> account = getAccount();
        Map<String, Object> profile = getProfile(account, "profile");

        if (profile == null || account == null) {
            return null;
        }

        return (String) profile.get("nickname");
    }

    @Override
    public String getImageUrl() {

        Map<String, Object> account = getAccount();
        Map<String, Object> profile = getProfile(account, "profile");

        if (profile == null || account == null) {
            return null;
        }

        return (String) profile.get("thumbnail_image_url");
    }

    private Map<String, Object> getAccount() {
        return getProfile(attributes, "kakao_account");
    }
}

