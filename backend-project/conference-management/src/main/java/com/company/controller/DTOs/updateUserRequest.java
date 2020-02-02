package com.company.controller.DTOs;

public class updateUserRequest
{
    private String username;

    private String email;

    private String password;

    private String discogsToken;

    public updateUserRequest(){}

    public String getDiscogsToken() {
        return discogsToken;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setDiscogsToken(String discogsToken) {
        this.discogsToken = discogsToken;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
