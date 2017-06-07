package com.company.controller.DTOs;

/**
 * Created by Alex on 07.06.2017.
 */
public class updateUserRequest
{
    private String username;
    private String name;
    private String affiliation;
    private String email;
    private String website;
    private String password;
    private Boolean isCommiteeMember=null;
    public updateUserRequest(){}

    public void setIsCommiteeMember(boolean commiteeMember) {
        isCommiteeMember = commiteeMember;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getWebsite() {
        return website;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public Boolean getisCommiteeMember() {
        return isCommiteeMember;
    }
}
