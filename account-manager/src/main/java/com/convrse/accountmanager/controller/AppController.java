package com.convrse.accountmanager.controller;
import com.convrse.accountmanagerlib.entity.App;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.convrse.accountmanagerlib.service.AppService;
import jakarta.validation.Valid;



@RestController
@RequestMapping("app")
public class AppController {
    
    private AppService appService;

    public AppController(AppService appService) {
        this.appService = appService;
    }

    @PostMapping("/")
    public ResponseEntity<Object> createApp(@Valid @RequestBody App app) {
        try {
            System.out.println("AppController.createApp: " + app.toString());
            App createdApp = appService.createApp(app);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdApp);
        } catch (IllegalArgumentException e) {
            // Handle duplicate value exception
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/list")
    public String listApps() throws UnirestException, JsonProcessingException {
       try {
           return appService.findAll().toString();
       }
       catch (Exception e) {
           return e.getMessage();
       }
    }
                
}
