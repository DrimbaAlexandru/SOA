package com.company.controller.DTOs;

/**
 * Created by Alex on 07.06.2017.
 */
public class loginRequest
{
    private String username;
    private String password;
    public loginRequest(){}
    public String getPassword() { return password; }
    public String getUsername() { return username; }
    public void setPassword(String password) { this.password = password; }
    public void setUsername(String username) { this.username = username; }
}
