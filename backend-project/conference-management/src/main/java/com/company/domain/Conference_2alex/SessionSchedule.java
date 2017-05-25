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
    @JoinColumn(name = "speakerID", nullable = false)
    private AppUser speaker;

    @ManyToMany
    @JoinTable(name="Listeners")
    private Set<AppUser> listeners;

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

    public AppUser getSpeaker() {
        return speaker;
    }

    public void setSpeaker(AppUser speaker) {
        this.speaker = speaker;
    }

    public SessionSchedule(){}

}