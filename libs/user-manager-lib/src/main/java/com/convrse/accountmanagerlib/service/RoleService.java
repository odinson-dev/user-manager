package com.convrse.accountmanagerlib.service;

import com.convrse.accountmanagerlib.entity.App;
import com.convrse.accountmanagerlib.entity.Role;
import com.convrse.accountmanagerlib.repository.RoleRepository;
import com.convrse.accountmanagerlib.requests.RoleRequest;

import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private  RoleRepository roleRepository;


    @Autowired
    private  AppService appService;


    public Role createRole(RoleRequest roleReq) {
        validateRoleUnique(roleReq.getRole(), roleReq.getAppId());
       
        appService.validateAppExists(roleReq.getAppId());
        App app = appService.getAppByAppId(roleReq.getAppId());
        Role role = new Role(roleReq.getRole(), app);
        return roleRepository.save(role);
    }

    private void validateRoleUnique(String role, Long appId) {
        if (roleRepository.existsByRoleAndAppId(role, appId)) {
            throw new IllegalArgumentException("Role already exists.");
        }
    }

    public void addPermission(String roleName,Long app_id,String permission) {
        Role role = roleRepository.findByRoleAndAppId(roleName, app_id);
        if(role==null) throw new IllegalArgumentException("Role name not found");
        this.addPermission(role, permission);
    }

    public void addPermission(Long roleId,String permission) {
        Role role = roleRepository.findById(roleId).orElse(null);
        if(role==null) throw new IllegalArgumentException("Role id not found");
        this.addPermission(role, permission);
    }

    public void validateRoleExists(Long roleId) {
        if(!roleRepository.existsById(roleId)){
            throw new IllegalArgumentException("Role id not found.");
        }
    }


    public void validateRoleExists(String role) {
        if(!roleRepository.existsByRole(role)){
            throw new IllegalArgumentException("Role id not found.");
        }
    }

    @Transactional
    public void addPermission(Role role,String permission) {
        role.addPermission(permission);
        roleRepository.save(role);
    }

    public List<String> getPermissionsByRoleId(Long roleId) {
        return roleRepository.findById(roleId).orElse(null).getPermissions();
    }

    public Role getRoleByRoleNameAndAppName(String roleName, String appName) {
        Long appId = appService.getAppIdByAppName(appName);
        return roleRepository.findByRoleAndAppId(roleName, appId);
    }

    public Iterable<Role> findAll() {
        return roleRepository.findAll();
    }
}