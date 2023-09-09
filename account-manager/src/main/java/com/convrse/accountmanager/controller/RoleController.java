package com.convrse.accountmanager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.convrse.accountmanagerlib.entity.Role;
import com.convrse.accountmanagerlib.requests.RoleRequest;
import com.convrse.accountmanagerlib.service.RoleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("role")
public class RoleController {
        private RoleService roleService;
        
        @PostMapping("/")
        public ResponseEntity<Object> createRole(@Valid @RequestBody RoleRequest roleReq) {
            try {
                Role createdRole = roleService.createRole(roleReq);
                return ResponseEntity.status(HttpStatus.CREATED).body(createdRole);
            } catch (IllegalArgumentException e) {
                // Handle duplicate value exception
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
        }

        @GetMapping("/list")
        public String listRoles() {
                return roleService.findAll().toString();
        }

        @PostMapping("/addPermission")
        public ResponseEntity<Object> addPermission(@RequestParam String role,@RequestParam Long appId, @RequestParam String permission) {
            try {
                roleService.addPermission(role,appId, permission);
                return ResponseEntity.status(HttpStatus.CREATED).body("Permission added");
            } catch (IllegalArgumentException e) {
                // Handle duplicate value exception
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
        }

        @PostMapping("/addPermissionByRoleId")
        public ResponseEntity<Object> addPermissionByRoleId(@RequestParam Long roleId, @RequestParam String permission) {
            try {
                roleService.addPermission(roleId, permission);
                return ResponseEntity.status(HttpStatus.CREATED).body("Permission added");
            } catch (IllegalArgumentException e) {
                // Handle duplicate value exception
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
        }

        @GetMapping("/permissions/byrole")
        public List<String> getPermissionsByRole(@RequestParam Long roleId) {
                return roleService.getPermissionsByRoleId(roleId);   
        }


}
