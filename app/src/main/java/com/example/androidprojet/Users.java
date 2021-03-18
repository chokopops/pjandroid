package com.example.androidprojet;

import java.io.Serializable;

public class Users implements Serializable {
    private String username;
    private String golf;
    private String email;
    private String role;
    private String photodeprofil;

    public Users(String username, String golf, String email, String role, String photodeprofil){
        this.username = username;
        this.golf = golf;
        this.email = email;
        this.role = role;
        this.photodeprofil = photodeprofil;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGolf() {
        return golf;
    }

    public void setGolf(String usergolf) {
        this.golf = golf;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhotodeprofil() {
        return photodeprofil;
    }

    public void setPhotodeprofil(String photodeprofil) {
        this.photodeprofil = photodeprofil;
    }
}

