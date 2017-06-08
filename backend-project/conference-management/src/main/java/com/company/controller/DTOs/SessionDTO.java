package com.company.controller.DTOs;

import com.company.controller.DTOs.utils_DTOs.SessionScheduleDTO;
import com.company.domain.Session;
import com.company.domain.SessionSchedule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sebi on 6/8/2017.
 */
public class SessionDTO {
    private int id;
    private String name;
    private List<SessionScheduleDTO> sessionScheduleDTOList;

    public SessionDTO(int id, String name, List<SessionScheduleDTO> sessionScheduleDTOList) {
        this.id = id;
        this.name = name;
        this.sessionScheduleDTOList = sessionScheduleDTOList;
    }

    public SessionDTO(){}

    public SessionDTO(Session s)
    {
        id=s.getId();
        name=s.getName();
        sessionScheduleDTOList=new ArrayList<>();
        for(SessionSchedule sch:s.getSchedule())
        {
            sessionScheduleDTOList.add(new SessionScheduleDTO(sch));
        }

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

    public List<SessionScheduleDTO> getSessionScheduleDTOList() {
        return sessionScheduleDTOList;
    }

    public void setSessionScheduleDTOList(List<SessionScheduleDTO> sessionScheduleDTOList) {
        this.sessionScheduleDTOList = sessionScheduleDTOList;
    }
}
