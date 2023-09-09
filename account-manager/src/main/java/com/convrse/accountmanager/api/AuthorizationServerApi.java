package com.convrse.accountmanager.api;
import com.convrse.accountmanagerlib.requests.LoginRequest;
import com.convrse.accountmanagerlib.response.UserLoginResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;

@Component
public class AuthorizationServerApi {

  public static UserLoginResponse loginUsingPasswordGrant(LoginRequest loginRequest) throws UnirestException, JsonProcessingException, URISyntaxException {

//    todo : fetch client_id and client_secret using app_name


    URI uri = new URIBuilder()
            .setScheme("http")
            .setHost("auth-server")
            .setPort(9002)
            .setPath("/oauth2/token")
            .setParameter("grant_type", "password")
            .setParameter("client_id", "client")
            .setParameter("client_secret", "secret")
            .setParameter("username", loginRequest.getEmail())
            .setParameter("password", loginRequest.getPassword())
            .setParameter("app", loginRequest.getApp())
            .build();

    HttpResponse<JsonNode> jsonRes = Unirest.post(uri.toString())
              .header("Content-Type", "application/x-www-form-urlencoded").asJson();



    if(jsonRes.getStatus() != 200) {
        throw new RuntimeException("AuthorizationServerApi.loginUsingPasswordGrant: " + "Invalid Login Credentials");
    }

    if(jsonRes.getBody().getObject().get("access_token") == null) {
        throw new RuntimeException("AuthorizationServerApi.loginUsingPasswordGrant: " + jsonRes.getBody().getObject().get("error_description"));
    }


      ObjectMapper objectMapper = new ObjectMapper();
      UserLoginResponse res = objectMapper.readValue(jsonRes.getBody().toString(), UserLoginResponse.class);
      return res;
  }
}
