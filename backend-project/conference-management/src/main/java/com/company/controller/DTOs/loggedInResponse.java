package com.company.controller.DTOs;

/**
 * Created by Alex on 07.06.2017.
 */
public class loggedInResponse
{
    private boolean isSuperUser=false;
    private boolean isCometeeMember =false;
    public loggedInResponse(){}
    public boolean getIsCometeeMember() { return isCometeeMember; }
    public boolean getIsSuperUser() { return isSuperUser; }

    public void setIsCometeeMember(boolean commiteeMember) {
        isCometeeMember = commiteeMember;
    }

    public void setIsSuperUser(boolean superUser) {
        isSuperUser = superUser;
    }
}
