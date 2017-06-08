package com.company.controller.DTOs.utils_DTOs;

import com.company.domain.SessionSchedule;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * Created by sebi on 6/8/2017.
 */
public class SessionScheduleDTO {
    private int sessionId, paperId;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date presentationStartTime, presentationEndTime;
    private String username;

    public SessionScheduleDTO(int sessionId, int paperId, Date presentationStartTime, Date presentationEndTime, String speakerName) {
        this.sessionId = sessionId;
        this.paperId = paperId;
        this.presentationStartTime = presentationStartTime;
        this.presentationEndTime = presentationEndTime;
        this.username = speakerName;
    }

    public SessionScheduleDTO(SessionSchedule s){
        sessionId = s.getId();
        paperId = s.getPaper().getId();
        username= s.getSpeaker().getUsername();
        presentationStartTime = s.getPresentationTime();
    }

    public SessionScheduleDTO(){}

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getPaperId() {
        return paperId;
    }

    public void setPaperId(int paperId) {
        this.paperId = paperId;
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
