package com.convrse.accountmanagerlib.entity;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;

@Entity
@Data
@Getter
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Role is mandatory")
    @Column(unique = true)
    private String role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_id", referencedColumnName = "id")
    private App app;

    @ElementCollection
    private List<String> permissions = new ArrayList<>();


    public Role(String role) {
        this.role = role;
    }

    public Role(String role, App app) {
        this.role = role;
        this.app = app;
    }

    public Role() {
    }


    public void addPermission(String permission) {
        if(this.permissions.contains(permission)) throw new IllegalArgumentException("Permission already exists");
        this.permissions.add(permission);
    }

    public List<String> removePermission(String permission) {
        if(!this.permissions.contains(permission)) throw new IllegalArgumentException("Permission does not exist");
        this.permissions.remove(permission);
        return this.permissions;
    }
}