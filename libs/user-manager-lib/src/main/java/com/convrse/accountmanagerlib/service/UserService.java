package com.convrse.accountmanagerlib.service;

import com.convrse.accountmanagerlib.entity.Role;
import com.convrse.accountmanagerlib.entity.User;
import com.convrse.accountmanagerlib.exceptions.UserNotFoundException;
import com.convrse.accountmanagerlib.repository.UserRepository;
import com.convrse.accountmanagerlib.requests.SignupRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private  RoleService roleService;

    @Autowired
    private  AppService appService;

    public User registerUser(SignupRequest signupReq) {

        String email, password, role, app;
        email = signupReq.getEmail();
        password = signupReq.getPassword();
        role = signupReq.getRole().isEmpty() ? "user" : signupReq.getRole();
        app = signupReq.getApp();

        validateUserNotExists(email, appService.getAppByAppName(app).getId());
        appService.validateAppExists(app);
        roleService.validateRoleExists(role);
        User user = new User(email, password, roleService.getRoleByRoleNameAndAppName(role, app), appService.getAppByAppName(app));
        return userRepository.save(user);
    }

    void validateUserExists(String email, Long appId) {
        if(!userRepository.existsByEmailAndAppId(email, appId)){
            throw new IllegalArgumentException("User not found.");
        }
    }

    void validateUserNotExists(String email, Long appId) {
        if(userRepository.existsByEmailAndAppId(email, appId)){
            throw new IllegalArgumentException("User is already registered.");
        }
    }

    public User getUserByEmailAndAppId(String email, Long appId) {
        appService.validateAppExists(appId);
        if(!userRepository.existsByEmailAndAppId(email, appId)){
            throw new UserNotFoundException("User not found.");
        }
        return userRepository.findByEmailAndAppId(email, appId);
    }

    public User getUserByEmailAndAppName(String email, String appName) {
        appService.validateAppExists(appName);
        Long appId = appService.getAppByAppName(appName).getId();
        return getUserByEmailAndAppId(email, appId);
    }


    public List<String> getUserPermissions(String email, Long appId) {
        Role role = userRepository.findRoleByEmailAndAppId(email, appId);
        return role.getPermissions();
    }
    public User getUserById(String id) {
        if(!userRepository.existsById(id)){
            throw new UserNotFoundException("User not found.");
        }
        return userRepository.findById(id).orElse(null);
    }


}