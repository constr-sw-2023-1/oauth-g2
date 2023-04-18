package br.com.grupo2.oauth.api.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RefreshToken {

    @JsonProperty("refresh-token")
    private String bearer;


    public RefreshToken(String refreshToken) {
        this.bearer = refreshToken;
    }

    public String getRefreshToken() {
        return bearer;
    }

    public void setRefreshToken(String refreshToken) {
        this.bearer = refreshToken;
    }
}
