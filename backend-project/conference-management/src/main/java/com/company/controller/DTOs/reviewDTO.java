package com.company.controller.DTOs;

import com.company.domain.Review;

/**
 * Created by Alex on 07.06.2017.
 */
public class reviewDTO
{
    private String status;
    private String justification;
    public reviewDTO(Review r)
    {
        if(r!=null){
        status=r.getStatus().name();
        justification=r.getJustification();
        }
    }

    public reviewDTO(){}
    public String getJustification() {
        return justification;
    }

    public String getStatus() {
        return status;
    }
}
