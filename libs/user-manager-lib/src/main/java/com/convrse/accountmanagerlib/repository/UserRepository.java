package com.convrse.accountmanagerlib.repository;

import com.convrse.accountmanagerlib.entity.Role;
import com.convrse.accountmanagerlib.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findById(String id);

    User findByEmailAndAppId(String email, Long appId);
    Boolean existsByEmailAndAppId(String email, Long appId);

    Role findRoleByEmailAndAppId(String email, Long appId);

}