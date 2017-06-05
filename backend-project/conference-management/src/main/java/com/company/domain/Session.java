package com.company.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Crisitan on 5/22/2017.
 */

@Table(name = "Sesion")
@Entity
public class Session {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY )
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", unique = false, nullable = false, length = 256)
    private String name;

    @ManyToOne
    @JoinColumn(name = "conf_ID", nullable = false)
    private Conference conference;

    @ManyToMany
    @JoinTable(name = "Listeners")
    private Set<AppUser> listeners = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "sessionChairID")
    private AppUser sessionChair;

    @OneToMany(mappedBy = "session")
    private Set<SessionSchedule> schedule;

    public Session(){}

    public Session(Conference conf, String name) {
        this.name = name;
        this.conference = conf;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Conference getConference() {
        return conference;
    }

    public void setConference(Conference conference) {
        this.conference = conference;
    }

    public Set<AppUser> getListeners() {
        return listeners;
    }

    public void setListeners(Set<AppUser> listeners) {
        this.listeners = listeners;
    }

    public AppUser getSessionChair() {
        return sessionChair;
    }

    public void setSessionChair(AppUser sesionChair) {
        this.sessionChair = sesionChair;
    }

    public Set<SessionSchedule> getSchedule() {
        return schedule;
    }

    public void setSchedule(Set<SessionSchedule> schedule) {
        this.schedule = schedule;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}