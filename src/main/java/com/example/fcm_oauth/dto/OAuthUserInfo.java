package com.example.fcm_oauth.dto;

import java.util.Map;

public abstract class OAuthUserInfo {

    protected Map<String, Object> attributes;

    public OAuthUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public abstract String getId();

    public abstract String getNickName();

    public abstract String getImageUrl();

}
