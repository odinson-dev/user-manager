package com.convrse.token;

import java.util.Base64;

public class BearerToken {
    String token;

    public  String decodeBearerToken() {
        String encodedToken = getValue();
        byte[] decodedToken = Base64.getDecoder().decode(encodedToken);
        String token = new String(decodedToken);
        return token;
    }


    public BearerToken(String token) {
        this.token = token;
    }

    public String getValue() {
        String[] parts = this.token.split(" ");
        return parts[1];
    }


}
