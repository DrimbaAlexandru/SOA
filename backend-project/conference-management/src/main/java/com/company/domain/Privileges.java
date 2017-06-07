package com.company.domain;

import javax.persistence.*;

/**
 * Created by Alex on 22.05.2017.
 */
@Table(name = "Privileges")
@Entity
public class Privileges {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY )
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "userID", nullable = false)
    private AppUser user;

    @ManyToOne
    @JoinColumn(name = "conferenceID", nullable = false)
    private Conference conference;

    @Column(name = "isPCMember", nullable = false)
    private Boolean isPCMember;

    @Column(name = "isChair", nullable = false)
    private Boolean isChair;

    @Column(name = "isCoChair", nullable = false)
    private Boolean isCoChair;

    @Column(name = "isAuthor", nullable = false)
    private Boolean isAuthor;

    public Privileges() {
        this.user = null;
        this.conference = null;
    }

    public Privileges(AppUser user, Conference conf) {
        isPCMember = false;
        isChair = false;
        isCoChair = false;
        isAuthor = false;
        this.user = user;
        this.conference = conf;
    }

    public Integer getId() {
        return id;
    }

    public Boolean getIsAuthor() {
        return isAuthor;
    }

    public Boolean getIsChair() {
        return isChair;
    }

    public Boolean getIsCoChair() {
        return isCoChair;
    }

    public Boolean getIsPCMember() {
        return isPCMember;
    }

    public void setIsAuthor(Boolean author) {
        isAuthor = author;
    }

    public void setIsChair(Boolean chair) {
        isChair = chair;
    }

    public void setIsCoChair(Boolean coChair) {
        isCoChair = coChair;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setIsPCMember(Boolean PCMember) {
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