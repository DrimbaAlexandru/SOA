package com.company.controller.DTOs;

import com.company.controller.DTOs.utils_DTOs.CallForAbstractTimeSpan;
import com.company.controller.DTOs.utils_DTOs.CallForProposalsTimeSpan;
import com.company.controller.DTOs.utils_DTOs.EventTimeSpan;
import com.company.domain.Conference;

import java.util.Date;

/**
 * Created by sebi on 6/7/2017.
 */
public class ConferenceDTO {
    private int id;
    private String name;
    private EventTimeSpan eventTimeSpan;
    private CallForAbstractTimeSpan callForAbstractTimeSpan;
    private CallForProposalsTimeSpan callForProposalsTimeSpan;
    private Date biddingDeadline;

    public ConferenceDTO(Conference conf) {
        this.id = conf.getId();
        this.name = conf.getName();
        this.biddingDeadline = conf.getBiddingDeadline();
    }

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

    public EventTimeSpan getEventTimeSpan() {
        return eventTimeSpan;
    }

    public void setEventTimeSpan(EventTimeSpan eventTimeSpan) {
        this.eventTimeSpan = eventTimeSpan;
    }

    public CallForAbstractTimeSpan getCallForAbstractTimeSpan() {
        return callForAbstractTimeSpan;
    }

    public void setCallForAbstractTimeSpan(CallForAbstractTimeSpan callForAbstractTimeSpan) {
        this.callForAbstractTimeSpan = callForAbstractTimeSpan;
    }

    public CallForProposalsTimeSpan getCallForProposalsTimeSpan() {
        return callForProposalsTimeSpan;
    }

    public void setCallForProposalsTimeSpan(CallForProposalsTimeSpan callForProposalsTimeSpan) {
        this.callForProposalsTimeSpan = callForProposalsTimeSpan;
    }

    public Date getBiddingDeadline() {
        return biddingDeadline;
    }

    public void setBiddingDeadline(Date biddingDeadline) {
        this.biddingDeadline = biddingDeadline;
    }
}
