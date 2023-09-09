package com.convrse.accountmanagerlib.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginResponse {
    private String access_token;
    private String refresh_token;
    private String token_type;
    private String expires_in;

    public UserLoginResponse(String access_token, String refresh_token, String token_type, String expires_in) {
        this.access_token = access_token;
        this.refresh_token = refresh_token;
        this.token_type = token_type;
        this.expires_in = expires_in;
    }

    public UserLoginResponse() {
    }
}
