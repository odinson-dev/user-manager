package com.convrse.accountmanagerlib.repository;

import com.convrse.accountmanagerlib.entity.Role;

import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<Role, Long> {
    boolean existsByRole(String role);
    boolean existsByRoleAndAppId(String role, Long appId);
    Role findByRoleAndAppId(String role, Long appId);

}