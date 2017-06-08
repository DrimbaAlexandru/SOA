package com.company.controller.DTOs;

import com.company.controller.DTOs.utils_DTOs.SessionSchedule;
import com.company.domain.Session;
import com.company.domain.Conference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by sebi on 6/8/2017.
 */
public class SessionDTO {
    private int id;
    private int conference;
    private String name;
    private List<SessionSchedule> sessionScheduleList;

    public SessionDTO(int id, String name, Conference conference, List<SessionSchedule> sessionScheduleList) {
        this.id = id;
        this.name = name;
        this.sessionScheduleList = sessionScheduleList;
        this.conference = conference.getId();
    }

    public SessionDTO(Session session)
    {
        this.id = session.getId();
        this.name = session.getName();
        this.conference = session.getConference().getId();
    }

    public SessionDTO(){}

    public int getConference(){return conference;}
    public void setConference(int c) {this.conference = c;}


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

    public List<SessionSchedule> getSessionScheduleList() {
        return sessionScheduleList;
    }

    public void setSessionScheduleList(List<SessionSchedule> sessionScheduleList) {
        this.sessionScheduleList = sessionScheduleList;
    }
}
