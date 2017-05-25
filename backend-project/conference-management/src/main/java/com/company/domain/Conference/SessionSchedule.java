package com.company.domain.Conference;

import javax.persistence.*;
<<<<<<< HEAD
import java.util.Date;
=======
>>>>>>> 81bc439640f04387779552afdd9a099384ff8748
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
<<<<<<< HEAD
    private Session session;

    @Column(name = "presentationTime")
    private Date presentationTime;

    @ManyToOne
    @JoinColumn(name = "speakerID", nullable = false)
    private AppUser speaker;
=======
    private Sesion sesion;

    @Column(name = "presentationTime")
    private long presentationTime;
>>>>>>> 81bc439640f04387779552afdd9a099384ff8748

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

<<<<<<< HEAD
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
=======
    public Sesion getSesion() {
        return sesion;
    }

    public void setSesion(Sesion sesion) {
        this.sesion = sesion;
    }

    public long getPresentationTime() {
        return presentationTime;
    }

    public void setPresentationTime(long presentationTime) {
        this.presentationTime = presentationTime;
    }

    public SessionSchedule(Paper paper, Sesion sesion, long presentationTime){
        this.paper = paper;
        this.sesion = sesion;
>>>>>>> 81bc439640f04387779552afdd9a099384ff8748
        this.presentationTime = presentationTime;
    }

    public SessionSchedule(){}

<<<<<<< HEAD
    public AppUser getSpeaker() {
        return speaker;
    }

    public void setSpeaker(AppUser speaker) {
        this.speaker = speaker;
    }
=======
>>>>>>> 81bc439640f04387779552afdd9a099384ff8748
}