package com.company.domain.Conference;

import javax.persistence.*;
<<<<<<< HEAD
import java.util.Date;
=======
>>>>>>> 81bc439640f04387779552afdd9a099384ff8748
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
    @Column(name = "id")
    private int id;

    @Column(name = "name", unique = false, nullable = false, length = 256)
    private String name;

<<<<<<< HEAD
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
=======
    @Column(name = "eventTimeSpan", nullable = false)
    private long eventTimeSpan;

    @Column(name = "callForAbstractTimeSpan", nullable = false)
    private long callForAbstractTimeSpan;

    @Column(name = "callForProposalsTimeSpan", nullable = false)
    private long callForProposalsTimeSpan;

    @Column(name = "biddingDeadline", nullable = false)
    private long biddingDeadline;

    @OneToMany(mappedBy = "conf_ID")
    private Set<Privileges> privileges = new HashSet<>();

    public Conference(){}

    public Conference(String name, long eventTimeSpan, long callForAbstractTimeSpan, long callForProposalsTimeSpan, long biddingDeadline) {
        this.name = name;
        this.eventTimeSpan = eventTimeSpan;
        this.callForAbstractTimeSpan = callForAbstractTimeSpan;
        this.callForProposalsTimeSpan = callForProposalsTimeSpan;
        this.biddingDeadline = biddingDeadline;
>>>>>>> 81bc439640f04387779552afdd9a099384ff8748
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

<<<<<<< HEAD
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
=======
    public long getEventTimeSpan() {
        return eventTimeSpan;
    }

    public void setEventTimeSpan(long eventTimeSpan) {
        this.eventTimeSpan = eventTimeSpan;
    }

    public long getCallForAbstractTimeSpan() {
        return callForAbstractTimeSpan;
    }

    public void setCallForAbstractTimeSpan(long callForAbstractTimeSpan) {
        this.callForAbstractTimeSpan = callForAbstractTimeSpan;
    }

    public long getCallForProposalsTimeSpan() {
        return callForProposalsTimeSpan;
    }

    public void setCallForProposalsTimeSpan(long callForProposalsTimeSpan) {
        this.callForProposalsTimeSpan = callForProposalsTimeSpan;
    }

    public long getBiddingDeadline() {
        return biddingDeadline;
    }

    public void setBiddingDeadline(long biddingDeadline) {
        this.biddingDeadline = biddingDeadline;
    }
}
>>>>>>> 81bc439640f04387779552afdd9a099384ff8748
