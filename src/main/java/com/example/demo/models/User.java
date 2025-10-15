package com.example.demo.models;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document( collection = "Users")
@TypeAlias("user")
public class User {
    @Id
    private String id;

    @NotNull(message = "El nombre no puede ser nulo")
    @Size(min = 3, max = 15, message = "El nombre debe tener entre 3 y 15 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9_.]+$", message = "El nombre solo puede contener letras, números, guiones bajos y puntos")
    private String name;

    @NotNull(message = "El mail no puede ser nulo")
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 8, max = 255, message = "La contraseña debe tener entre 8 y 255 caracteres")

    private String password;
    private String userGroup;
    private String authToken;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }

    private String generarAPi_Token() {
        return UUID.randomUUID().toString();
    }


    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", userGroup='" + userGroup + '\'' +
                ", authToken='" + authToken + '\'' +
                '}';
    }

}
