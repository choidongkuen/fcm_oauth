package com.example.fcm_oauth.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MemberException extends RuntimeException {

    private final ErrorCode errorCode;
}
