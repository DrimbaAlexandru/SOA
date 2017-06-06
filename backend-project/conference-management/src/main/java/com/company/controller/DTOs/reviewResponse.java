package com.company.controller.DTOs;

import com.company.domain.Review;

/**
 * Created by Alex on 07.06.2017.
 */
public class reviewResponse
{
    private String status;
    private String justification;
    public reviewResponse(Review r)
    {
        if(r!=null){
        status=r.getStatus().name();
        justification=r.getJustification();
        }
    }

    public reviewResponse(){}
    public String getJustification() {
        return justification;
    }

    public String getStatus() {
        return status;
    }
}
