package com.convrse.accountmanager.controller;

import com.convrse.accountmanager.api.AuthorizationServerApi;
import com.convrse.accountmanagerlib.requests.LoginRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.exceptions.UnirestException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.convrse.accountmanagerlib.service.UserService;

import java.net.URISyntaxException;

@RestController
@RequestMapping("login")
public class LoginController {

    UserService userService;

    @PostMapping("/")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginRequest loginReq) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(AuthorizationServerApi.loginUsingPasswordGrant(loginReq));
        } catch (IllegalArgumentException e) {
            // Handle duplicate value exception
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException | URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
