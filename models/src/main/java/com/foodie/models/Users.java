package com.foodie.models;

import java.io.Serializable;

import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import org.springframework.stereotype.Service;

import java.util.UUID;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor @Getter @Setter
public class Users implements Serializable{
    @GeneratedValue(strategy = GenerationType.UUID)
    private @Id @Setter(AccessLevel.PROTECTED) UUID id;
    private String username;
    private String password;
    private String email;
    private String role;
    private String status;
    private String created_at;
    private String updated_at;

    public Users(String username, String password, String email, String role, String status, String created_at, String updated_at) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.status = status;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

}
