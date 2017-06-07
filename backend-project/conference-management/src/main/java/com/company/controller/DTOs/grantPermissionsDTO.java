package com.company.controller.DTOs;

/**
 * Created by Alex on 07.06.2017.
 */
public class grantPermissionsDTO
{
    private Boolean isPCMember=null;
    private Boolean isChair=null;
    private Boolean isCoChair=null;

    public grantPermissionsDTO(){}

    public void setIsChair(boolean chair) {
        isChair = chair;
    }

    public void setIsCoChair(boolean coChair) {
        isCoChair = coChair;
    }

    public void setIsPCMember(boolean PCMember) {
        isPCMember = PCMember;
    }

    public Boolean getIsChair() {
        return isChair;
    }

    public Boolean getIsCoChair() {
        return isCoChair;
    }

    public Boolean getIsPCMember() {
        return isPCMember;
    }
}
