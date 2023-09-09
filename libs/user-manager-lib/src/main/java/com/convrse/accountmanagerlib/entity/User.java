package com.convrse.accountmanagerlib.entity;
import com.convrse.accountmanagerlib.utils.IdGeneration;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class User {

    @Id
    @Column(unique = true)
    private String id;
    private String email;
    private String username;

    @Column(length = 60)
    private String password;

   @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_id", referencedColumnName = "id")
    private App app;
    private boolean enabled = false;


    public User(String email, String password, Role role, App app) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.app = app;
        this.id = IdGeneration.generateNanoId();
        this.enabled = true;
    }

    public User() {
    }

}