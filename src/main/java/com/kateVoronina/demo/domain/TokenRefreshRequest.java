package com.kateVoronina.demo.domain;

public class TokenRefreshRequest {
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public TokenRefreshRequest(String token) {
        this.token = token;
    }
}
