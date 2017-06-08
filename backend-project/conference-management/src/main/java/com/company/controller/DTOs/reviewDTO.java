package com.company.controller.DTOs;

import com.company.domain.Review;

/**
 * Created by Alex on 07.06.2017.
 */
public class reviewDTO
{
    private String status;
    private String justification;
    private String username;
    public reviewDTO(Review r)
    {
        if(r!=null){
            status=r.getStatus().name();
            justification=r.getJustification();
            username=r.getReviewer().getUsername();
        }
    }

    public reviewDTO(){}
    public String getJustification() {
        return justification;
    }

    public String getStatus() {
        return status;
    }

    public String getUsername() {
        return username;
    }

}
