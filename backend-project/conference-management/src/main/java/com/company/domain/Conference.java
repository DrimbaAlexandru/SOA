package com.company.domain;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Crisitan on 5/22/2017.
 */

@Table(name = "Conference")
@Entity
public class Conference {
    @Id
    @GeneratedValue
    @Column(name = "conf_id")
    private int id;

    @Column(name = "name", unique = false, nullable = false, length = 256)
    private String name;

    @Column(name = "eventStartDate", nullable = false)
    private Date eventStartDate;

    @Column(name = "eventEndDate", nullable = false)
    private Date eventEndDate;

    @Column(name = "callForAbstractDeadline", nullable = false)
    private Date callForAbstractDeadline;

    @Column(name = "callForProposalsDeadline", nullable = false)
    private Date callForProposalsDeadline;

    @Column(name = "biddingDeadline", nullable = false)
    private Date biddingDeadline;

    @OneToMany(mappedBy = "conference")
    private Set<Privileges> privileges = new HashSet<>();

    @OneToMany(mappedBy = "conference")
    private Set<Session> sessions=new HashSet<>();

    public Conference(){}

    public Conference(String name, Date eventStart, Date eventEnd, Date callForAbstractDeadline, Date callForProposalsDeadline, Date biddingDeadline) {
        this.name = name;
        this.biddingDeadline=biddingDeadline;
        this.callForAbstractDeadline=callForAbstractDeadline;
        this.callForProposalsDeadline=callForProposalsDeadline;
        this.eventEndDate=eventEnd;
        this.eventStartDate=eventStart;
    }

    public Set<Privileges> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(Set<Privileges> privileges) {
        this.privileges = privileges;
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

    public Date getBiddingDeadline() {
        return biddingDeadline;
    }

    public Date getCallForAbstractDeadline() {
        return callForAbstractDeadline;
    }

    public Date getCallForProposalsDeadline() {
        return callForProposalsDeadline;
    }

    public Date getEventEndDate() {
        return eventEndDate;
    }

    public Date getEventStartDate() {
        return eventStartDate;
    }

    public Set<Session> getSessions() {
        return sessions;
    }

    public void setBiddingDeadline(Date biddingDeadline) {
        this.biddingDeadline = biddingDeadline;
    }

    public void setCallForAbstractDeadline(Date callForAbstractDeadline) {
        this.callForAbstractDeadline = callForAbstractDeadline;
    }

    public void setCallForProposalsDeadline(Date callForProposalsDeadline) {
        this.callForProposalsDeadline = callForProposalsDeadline;
    }

    public void setEventEndDate(Date eventEndDate) {
        this.eventEndDate = eventEndDate;
    }

    public void setEventStartDate(Date eventStartDate) {
        this.eventStartDate = eventStartDate;
    }

    public void setSessions(Set<Session> sessions) {
        this.sessions = sessions;
    }
}