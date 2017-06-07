package com.company.controller.DTOs;

import com.company.controller.DTOs.utils_DTOs.SessionSchedule;

import java.util.List;

/**
 * Created by sebi on 6/8/2017.
 */
public class SessionDTO {
    private int id;
    private String name;
    private List<SessionSchedule> sessionScheduleList;

    public SessionDTO(int id, String name, List<SessionSchedule> sessionScheduleList) {
        this.id = id;
        this.name = name;
        this.sessionScheduleList = sessionScheduleList;
    }

    public SessionDTO(){}

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
