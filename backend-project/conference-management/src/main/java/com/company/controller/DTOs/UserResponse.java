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
    public UserResponse(){}
    public UserResponse(AppUser u)
    {
        username=u.getUsername();
        name=u.getName();
        affiliation=u.getAffiliation();
        email=u.getEmail();
        website=u.getWebpage();
    }
    public UserResponse(String _username,String _name,String _affiliation, String _email,String _website)
    {
        username=_username;
        name=_name;
        affiliation=_affiliation;
        email=_email;
        website=_website;
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
}
