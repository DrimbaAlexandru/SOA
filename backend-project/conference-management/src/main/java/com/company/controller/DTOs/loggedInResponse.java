package com.company.controller.DTOs;

/**
 * Created by Alex on 07.06.2017.
 */
public class loggedInResponse
{
    private boolean isSuperUser=false;
    private boolean isCommiteeMember=false;
    public loggedInResponse(){}
    public boolean isCommiteeMember() { return isCommiteeMember; }
    public boolean isSuperUser() { return isSuperUser; }

    public void setCommiteeMember(boolean commiteeMember) {
        isCommiteeMember = commiteeMember;
    }

    public void setSuperUser(boolean superUser) {
        isSuperUser = superUser;
    }
}
