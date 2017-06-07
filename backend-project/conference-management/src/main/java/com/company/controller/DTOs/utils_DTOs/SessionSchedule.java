package com.company.controller.DTOs.utils_DTOs;

import java.util.Date;

/**
 * Created by sebi on 6/8/2017.
 */
public class SessionSchedule {
    private int sessionId, papaerId;
    private Date presentationStartTime, presentationEndTime;

    public SessionSchedule(int sessionId, int papaerId, Date presentationStartTime, Date presentationEndTime, String speakerName) {
        this.sessionId = sessionId;
        this.papaerId = papaerId;
        this.presentationStartTime = presentationStartTime;
        this.presentationEndTime = presentationEndTime;
        this.speakerName = speakerName;
    }
    public SessionSchedule(){}

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getPapaerId() {
        return papaerId;
    }

    public void setPapaerId(int papaerId) {
        this.papaerId = papaerId;
    }

    public Date getPresentationStartTime() {
        return presentationStartTime;
    }

    public void setPresentationStartTime(Date presentationStartTime) {
        this.presentationStartTime = presentationStartTime;
    }

    public Date getPresentationEndTime() {
        return presentationEndTime;
    }

    public void setPresentationEndTime(Date presentationEndTime) {
        this.presentationEndTime = presentationEndTime;
    }

    public String getSpeakerName() {
        return speakerName;
    }

    public void setSpeakerName(String speakerName) {
        this.speakerName = speakerName;
    }

    private String speakerName;

}
