package com.convrse.accountmanagerlib.service;

import com.convrse.accountmanagerlib.entity.App;
import com.convrse.accountmanagerlib.exceptions.AppNotFoundException;
import com.convrse.accountmanagerlib.exceptions.DuplicateAppIdException;
import com.convrse.accountmanagerlib.repository.AppRepository;
import org.springframework.stereotype.Service;

@Service
public class AppService {

    private AppRepository appRepository;

    public AppService(AppRepository appRepository) {
        this.appRepository = appRepository;
    }
    public App createApp(App app) {
        validateAppNameUnique(app.getName());
        return appRepository.save(app);
    }

    private void validateAppNameUnique(String appName) {
        if (appRepository.existsByName(appName)) {
            throw new DuplicateAppIdException("App with the same name already exists.");
        }
    }

    public void validateAppExists(App app) {
        if(app==null || !appRepository.existsById(app.getId())){
            throw new AppNotFoundException("App does not exist.");
        }
    }

     public void validateAppExists(String app) {
        if(app==null || !appRepository.existsByName(app)){
            throw new AppNotFoundException("App does not exist.");
        }
    }


    public void validateAppExists(Long appId) {
        if(!appRepository.existsById(appId)){
            throw new AppNotFoundException("App does not exist.");
        }
    }

    App getAppByAppId(Long appId) {
        return appRepository.findById(appId).orElse(null);
    }

    App getAppByAppName(String appName) {
        return appRepository.findByName(appName);
    }

    Long getAppIdByAppName(String appName) {
        return appRepository.findByName(appName).getId();
    }

    public Iterable<App> findAll() {
        return appRepository.findAll();
    }
}