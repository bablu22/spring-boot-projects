package com.bablumia.store.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginResponse {
    private final String accessToken;
    private final String refreshToken;
}
