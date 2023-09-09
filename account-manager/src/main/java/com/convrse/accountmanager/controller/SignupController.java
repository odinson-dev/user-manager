package com.convrse.accountmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.convrse.accountmanagerlib.requests.SignupRequest;
import com.convrse.accountmanagerlib.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("signup")
public class SignupController {
    

    @Autowired
    private UserService userService;
    
    @PostMapping("/")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody SignupRequest signupReq) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(signupReq));
            } catch (IllegalArgumentException e) {
                // Handle duplicate value exception
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
    }

}
