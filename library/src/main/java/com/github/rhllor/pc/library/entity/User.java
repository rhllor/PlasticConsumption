package com.github.rhllor.pc.library.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {

    public User() {}
    public User(Long id, String codUser) {
        this.setId(id);
        this.setUsername(codUser);
        this.setPassword("password");
    }

    private @Id @GeneratedValue(strategy = GenerationType.AUTO) Long id;
    private String username;
    private String password;
    private String token;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + this.id + ", codUser='" + this.username + "'}";
    }
}
