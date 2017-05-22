package com.company.domain.Conference;

import javax.persistence.*;
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
