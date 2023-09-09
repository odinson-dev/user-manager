package com.convrse.accountmanagerlib.repository;

import com.convrse.accountmanagerlib.entity.App;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AppRepository extends JpaRepository<App, Long> {
    App findByName(String name);
    Boolean existsByName(String name);
    
}
