package com.company.domain.Conference;

import javax.persistence.*;
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
    private Sesion sesion;

    @Column(name = "presentationTime")
    private long presentationTime;

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
        this.presentationTime = presentationTime;
    }

    public SessionSchedule(){}

}