package com.company.domain.Conference;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Alex on 21.05.2017.
 */
@Table(name = "SessionSchedule")
@Entity
public class SessionSchedule {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @OneToOne
    @JoinColumn(name = "PaperID")
    private Paper paper;

    @ManyToOne
    @JoinColumn(name = "sesionID", nullable = false)
    private Session session;

    @Column(name = "presentationTime")
    private Date presentationTime;

    @ManyToOne
    @JoinColumn(name = "speakerID", nullable = false)
    private AppUser speaker;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setPaper(Paper paper) {
        this.paper = paper;
    }


    public Paper getPaper() {
        return paper;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Date getPresentationTime() {
        return presentationTime;
    }

    public void setPresentationTime(Date presentationTime) {
        this.presentationTime = presentationTime;
    }

    public SessionSchedule(Paper paper, Session sesion, Date presentationTime){
        this.paper = paper;
        this.session = sesion;
        this.presentationTime = presentationTime;
    }

    public SessionSchedule(){}

    public AppUser getSpeaker() {
        return speaker;
    }

    public void setSpeaker(AppUser speaker) {
        this.speaker = speaker;
    }
}