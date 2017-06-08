package com.company.controller.DTOs;
import com.company.controller.DTOs.utils_DTOs.TimeSpan;
import com.company.domain.Conference;

import java.util.Date;

/**
 * Created by sebi on 6/7/2017.
 */
public class ConferenceDTO {
    private Integer id;
    private String name;
    private TimeSpan eventTimeSpan;
    private TimeSpan callForAbstractTimeSpan;
    private TimeSpan callForProposalsTimeSpan;
    private Date biddingDeadline;

    public ConferenceDTO(Conference conf) {
        eventTimeSpan = new TimeSpan();
        callForAbstractTimeSpan = new TimeSpan();
        callForProposalsTimeSpan = new TimeSpan();
        this.id = conf.getId();
        this.name = conf.getName();
        this.biddingDeadline = conf.getBiddingDeadline();
        eventTimeSpan.setEndDate(conf.getEventEndDate());
        eventTimeSpan.setStartDate(conf.getEventStartDate());
        callForAbstractTimeSpan.setStartDate(new Date());
        callForProposalsTimeSpan.setStartDate(new Date());
        callForAbstractTimeSpan.setEndDate(conf.getCallForAbstractDeadline());
        callForProposalsTimeSpan.setEndDate(conf.getCallForProposalsDeadline());
    }
    public ConferenceDTO(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TimeSpan getEventTimeSpan() {
        return eventTimeSpan;
    }

    public void setEventTimeSpan(TimeSpan eventTimeSpan) {
        this.eventTimeSpan = eventTimeSpan;
    }

    public TimeSpan getCallForAbstractTimeSpan() {
        return callForAbstractTimeSpan;
    }

    public void setCallForAbstractTimeSpan(TimeSpan callForAbstractTimeSpan) {
        this.callForAbstractTimeSpan = callForAbstractTimeSpan;
    }

    public TimeSpan getCallForProposalsTimeSpan() {
        return callForProposalsTimeSpan;
    }

    public void setCallForProposalsTimeSpan(TimeSpan callForProposalsTimeSpan) {
        this.callForProposalsTimeSpan = callForProposalsTimeSpan;
    }

    public Date getBiddingDeadline() {
        return biddingDeadline;
    }

    public void setBiddingDeadline(Date biddingDeadline) {
        this.biddingDeadline = biddingDeadline;
    }
}
