package com.company.domain.Conference;

import org.hibernate.annotations.Check;

import javax.persistence.*;

/**
 * Created by Alex on 22.05.2017.
 */
@Table(name = "Privileges")
@Entity
public class Privileges {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "userID", nullable = false)
    private AppUser user;

    @ManyToOne
    @JoinColumn(name = "conferenceID", nullable = false)
    private Conference conference;

    @Column(name = "isPCMember", nullable = false)
    private boolean isPCMember;

    @Column(name = "isChair", nullable = false)
    private boolean isChair;

    @Column(name = "isCoChair", nullable = false)
    private boolean isCoChair;

    @Column(name = "isAuthor", nullable = false)
    private boolean isAuthor;

    public int getId() {
        return id;
    }

    public boolean isAuthor() {
        return isAuthor;
    }

    public boolean isChair() {
        return isChair;
    }

    public boolean isCoChair() {
        return isCoChair;
    }

    public boolean isPCMember() {
        return isPCMember;
    }

    public void setAuthor(boolean author) {
        isAuthor = author;
    }

    public void setChair(boolean chair) {
        isChair = chair;
    }

    public void setCoChair(boolean coChair) {
        isCoChair = coChair;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPCMember(boolean PCMember) {
        isPCMember = PCMember;
    }

    public AppUser getUser() {
        return user;
    }

    public Conference getConference() {
        return conference;
    }

    public void setConference(Conference conference) {
        this.conference = conference;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }
}
