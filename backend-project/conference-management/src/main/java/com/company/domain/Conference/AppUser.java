package com.company.domain.Conference;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Alex on 21.05.2017.
 */

@Table(name = "AppUser")
@Entity
public class AppUser {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

<<<<<<< HEAD
    @Column(name = "username",unique = true, nullable = false, length = 64)
    private String username;

    @Column(name = "name",nullable = false, length = 64)
    private String name;

    @Column(name = "affiliation",length = 64)
    private String affiliation;

    @Column(name = "email",unique = true, nullable = false, length = 64)
=======
    @Column(name = "username",unique = true, nullable = false)
    private String usrName;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "affiliation")
    private String affiliation;

    @Column(name = "emali",unique = true, nullable = false)
>>>>>>> 81bc439640f04387779552afdd9a099384ff8748
    private String email;

    @Column(name = "webpage")
    private String webpage;

<<<<<<< HEAD
    @Column(name = "password", nullable = false,length = 64)
    private String password;

    @Column(name = "isSuperUser", nullable = false)
    private boolean isSuperUser;

    @Column(name = "isCometeeMember", nullable = false)
=======
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "isSuperUser")
    private boolean isSuperUser;

    @Column(name = "isCometeeMember")
>>>>>>> 81bc439640f04387779552afdd9a099384ff8748
    private boolean isCometeeMember;

    @ManyToMany
    @JoinTable(name="SubmittedPapers")
    private Set<Paper> submittedPapers=new HashSet<>();

<<<<<<< HEAD
    @ManyToMany
    @JoinTable(name="AssignedForReview")
    private Set<Paper> assignedForReview=new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Privileges> privileges = new HashSet<>();

    @OneToMany(mappedBy = "reviewer")
    private Set<Review> reviews=new HashSet<>();

    @OneToMany(mappedBy = "bidder")
    private Set<Bid> bids=new HashSet<>();

    @OneToOne(mappedBy = "sessionChair")
    private Session sesionIsChairFor;

    @ManyToMany(mappedBy = "listeners")
    private Set<Session> listenedSessions = new HashSet<>();

    @OneToMany(mappedBy = "speaker")
    private Set<SessionSchedule> presentationsIsSpeakerFor = new HashSet<>();

    public Session getSesionIsChairFor() {
        return sesionIsChairFor;
    }

    public void setSesionIsChairFor(Session sesionIsChairFor) {
        this.sesionIsChairFor = sesionIsChairFor;
    }

    public Set<Session> getListenedSessions() {
        return listenedSessions;
    }

    public void setListenedSessions(Set<Session> listenedSessions) {
        this.listenedSessions = listenedSessions;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
=======
    @OneToMany(mappedBy = "user_ID")
    private Set<Privileges> privileges = new HashSet<>();

    @OneToOne(mappedBy = "sesionChair")

    private int sesionChair;

    @ManyToMany(mappedBy = "Listeners")
    private Set<Sesion> sesions = new HashSet<>();


    public int getSesionChair() {
        return sesionChair;
    }

    public void setSesionChair(int sesionChair) {
        this.sesionChair = sesionChair;
    }

    public Set<Sesion> getSesions() {
        return sesions;
    }

    public void setSesions(Set<Sesion> sesions) {
        this.sesions = sesions;
    }


    public String getUsrName() {
        return usrName;
    }

    public void setUsrName(String usrName) {
        this.usrName = usrName;
>>>>>>> 81bc439640f04387779552afdd9a099384ff8748
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebpage() {
        return webpage;
    }

    public void setWebpage(String webpage) {
        this.webpage = webpage;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isSuperUser() {
        return isSuperUser;
    }

    public void setSuperUser(boolean superUser) {
        isSuperUser = superUser;
    }

    public boolean isCometeeMember() {
        return isCometeeMember;
    }

    public void setCometeeMember(boolean cometeeMember) {
        isCometeeMember = cometeeMember;
    }

    public Set<Privileges> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(Set<Privileges> privileges) {
        this.privileges = privileges;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setSubmittedPapers(Set<Paper> submittedPapers) {
        this.submittedPapers = submittedPapers;
    }

    public Set<Paper> getSubmittedPapers() {
        return submittedPapers;
    }

    public AppUser(){}

<<<<<<< HEAD
    public AppUser(String username, String name, String affiliation, String email, String webpage, String password) {
        this.username = username;
=======
    public AppUser(String usrName, String name, String affiliation, String email, String webpage, String password) {
        this.usrName = usrName;
>>>>>>> 81bc439640f04387779552afdd9a099384ff8748
        this.name = name;
        this.affiliation = affiliation;
        this.email = email;
        this.webpage = webpage;
        this.password = password;
    }

<<<<<<< HEAD
    public AppUser(String username, String name, String affiliation, String email,
                   String webpage, String password, boolean isSuperUser, boolean isCometeeMember) {
        this.username = username;
=======
    public AppUser(String usrName, String name, String affiliation, String email,
                   String webpage, String password, boolean isSuperUser, boolean isCometeeMember) {
        this.usrName = usrName;
>>>>>>> 81bc439640f04387779552afdd9a099384ff8748
        this.name = name;
        this.affiliation = affiliation;
        this.email = email;
        this.webpage = webpage;
        this.password = password;
        this.isSuperUser = isSuperUser;
        this.isCometeeMember = isCometeeMember;
    }
<<<<<<< HEAD

    public Set<Bid> getBids() {
        return bids;
    }

    public Set<Paper> getAssignedForReview() {
        return assignedForReview;
    }

    public Set<Review> getReviews() {
        return reviews;
    }

    public Set<SessionSchedule> getPresentationsIsSpeakerFor() {
        return presentationsIsSpeakerFor;
    }

    public void setAssignedForReview(Set<Paper> assignedForReview) {
        this.assignedForReview = assignedForReview;
    }

    public void setBids(Set<Bid> bids) {
        this.bids = bids;
    }

    public void setPresentationsIsSpeakerFor(Set<SessionSchedule> presentationsIsSpeakerFor) {
        this.presentationsIsSpeakerFor = presentationsIsSpeakerFor;
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }
}
=======
}
>>>>>>> 81bc439640f04387779552afdd9a099384ff8748
