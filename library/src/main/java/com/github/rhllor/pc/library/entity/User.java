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
        this.setCodUser(codUser);
    }

    private @Id @GeneratedValue(strategy = GenerationType.AUTO) Long id;
    private String codUser;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getCodUser() {
        return this.codUser;
    }

    public void setCodUser(String codUser) {
        this.codUser = codUser;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + this.id + ", codUser='" + this.codUser + "'}";
    }
}
