package com.convrse.accessprovider.controller;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.convrse.token.BearerToken;
import com.convrse.usermanagerlib.entity.App;
import com.convrse.usermanagerlib.entity.User;
import com.convrse.usermanagerlib.exceptions.UserNotFoundException;
import com.convrse.usermanagerlib.service.UserService;
import com.convrse.utils.JwtVerifier;
import com.mashape.unirest.http.exceptions.UnirestException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.convrse.accessprovider.api.AuthorizationServerApi;
import com.convrse.usermanagerlib.service.AppService;

import jakarta.validation.Valid;

import java.util.ArrayList;


@RestController
@RequestMapping("access-provider")
public class PermissionIssuerController {
    
    private UserService userService;

    public PermissionIssuerController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/get-permissions")
    public ResponseEntity<Object> getPermissions(@RequestHeader("Authorization") String bearerToken) {
        try {
            System.out.println("Bearer Token: " + bearerToken);
            JwtVerifier  jwtVerifier = new JwtVerifier();
            DecodedJWT decodedJWT = jwtVerifier.verifyBearerToken(new BearerToken(bearerToken));
            String uid = decodedJWT.getSubject();
            User user = userService.getUserById(uid);
            System.out.println("User: " + user.getEmail());
            return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<>());
        } catch (IllegalArgumentException e) {
            // Handle duplicate value exception
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (JWTVerificationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }




}
