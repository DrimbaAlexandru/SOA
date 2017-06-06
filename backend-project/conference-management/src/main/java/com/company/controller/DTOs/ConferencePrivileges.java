package com.company.controller.DTOs;

import com.company.domain.Privileges;

/**
 * Created by Alex on 07.06.2017.
 */
public class ConferencePrivileges
{
    private boolean isAuthor=false;
    private boolean isChair=false;
    private boolean isCoChair=false;
    private boolean isPCMember=false;
    public ConferencePrivileges(){}
    public ConferencePrivileges(Privileges p)
    {
        isAuthor=p.getIsAuthor();
        isChair=p.getIsChair();
        isCoChair=p.getIsCoChair();
        isPCMember=p.getIsPCMember();
    }

    public boolean getIsPCMember() {
        return isPCMember;
    }

    public boolean getIsCoChair() {
        return isCoChair;
    }

    public boolean getIsChair() {
        return isChair;
    }

    public boolean getIsAuthor() {
        return isAuthor;
    }

    public void setIsAuthor(boolean author) {
        isAuthor = author;
    }

    public void setIsChair(boolean chair) {
        isChair = chair;
    }

    public void setIsCoChair(boolean coChair) {
        isCoChair = coChair;
    }

    public void setIsPCMember(boolean PCMember) {
        isPCMember = PCMember;
    }
}
