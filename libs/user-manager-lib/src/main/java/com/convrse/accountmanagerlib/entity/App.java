package com.convrse.accountmanagerlib.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Getter
@Setter
public class App {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "App name is mandatory")
    @Column(unique = true, nullable = false, length = 60 )
    private String name;

    public String toString() {
        return "App(id=" + this.getId() + ", name=" + this.getName() + ")";
    }

    public App(String name) {
        this.name = name;
    }

    public App() {
    }

}