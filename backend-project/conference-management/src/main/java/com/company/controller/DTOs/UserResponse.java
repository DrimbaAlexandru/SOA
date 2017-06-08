package com.company.controller.DTOs;

import com.company.domain.AppUser;

/**
 * Created by Alex on 07.06.2017.
 */
public class UserResponse
{
    private String username="";
    private String name="";
    private String affiliation="";
    private String email="";
    private String website="";
    private boolean isSuperUser;
    private boolean isCometeeMember;
    public UserResponse(){}
    public UserResponse(AppUser u)
    {
        username=u.getUsername();
        name=u.getName();
        affiliation=u.getAffiliation();
        email=u.getEmail();
        website=u.getWebpage();
        isCometeeMember=u.getIsCometeeMember();
        isSuperUser=u.getIsSuperUser();
    }

    public String getUsername() {
        return username;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getWebsite() {
        return website;
    }

    public boolean getIsCometeeMember() {
        return isCometeeMember;
    }

    public boolean getIsSuperUser() {
        return isSuperUser;
    }

}
