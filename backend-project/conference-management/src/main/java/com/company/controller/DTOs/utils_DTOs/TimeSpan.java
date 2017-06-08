package com.company.controller.DTOs.utils_DTOs;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * Created by sebi on 6/7/2017.
 */

public class TimeSpan {
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date startDate;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date endDate;

    public TimeSpan(){}

    public TimeSpan(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
