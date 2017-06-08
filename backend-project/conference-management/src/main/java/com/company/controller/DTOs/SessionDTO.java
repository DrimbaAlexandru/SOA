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
    private int conference;
    private String name;
    private List<SessionScheduleDTO> schedule;

    public SessionDTO(int id, String name, List<SessionScheduleDTO> sessionScheduleDTOList) {
        this.id = id;
        this.name = name;
        this.schedule = sessionScheduleDTOList;
    }

    public SessionDTO(){}

    public SessionDTO(Session s)
    {
        id=s.getId();
        name=s.getName();
        schedule =new ArrayList<>();
        for(SessionSchedule sch:s.getSchedule())
        {
            schedule.add(new SessionScheduleDTO(sch));
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

    public List<SessionScheduleDTO> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<SessionScheduleDTO> schedule) {
        this.schedule = schedule;
    }
}
