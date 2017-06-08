package com.company.controller.DTOs;

/**
 * Created by Alex on 07.06.2017.
 */
public class loggedInResponse
{
    private boolean isSuperUser=false;
    private boolean isCommiteeMember=false;
    public loggedInResponse(){}
    public boolean getIsCommiteeMember() { return isCommiteeMember; }
    public boolean getIsSuperUser() { return isSuperUser; }

    public void setIsCommiteeMember(boolean commiteeMember) {
        isCommiteeMember = commiteeMember;
    }

    public void setIsSuperUser(boolean superUser) {
        isSuperUser = superUser;
    }
}
