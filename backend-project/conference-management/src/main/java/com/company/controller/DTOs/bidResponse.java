package com.company.controller.DTOs;

import com.company.domain.Bid;

/**
 * Created by Alex on 07.06.2017.
 */
public class bidResponse
{
    private String status;
    public bidResponse(){}

    public bidResponse(Bid motherfucker)
    {
        if(motherfucker!=null)
            status=motherfucker.getStatus().name();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
