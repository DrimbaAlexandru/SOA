package com.company.domain.Conference;

<<<<<<< HEAD
import org.hibernate.annotations.Check;

import javax.persistence.*;

/**
 * Created by Alex on 22.05.2017.
 */
=======
import javax.persistence.*;

/**
 * Created by Crisitan on 5/22/2017.
 */

>>>>>>> 81bc439640f04387779552afdd9a099384ff8748
@Table(name = "Privileges")
@Entity
public class Privileges {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @ManyToOne
<<<<<<< HEAD
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
=======
    @JoinColumn(name = "conf_ID", nullable = false)
    private Conference conf;

    @ManyToOne
    @JoinColumn(name = "user_ID", nullable = false)
    private AppUser usr;

    @Column(name = "isAutor")
    private boolean isAutor;

    @Column(name = "isPCMember")
    private boolean isPCMember;

    @Column(name = "isChair")
    private boolean isChair;

    @Column(name = "isCoChair")
    private boolean isCoChair;

    Privileges(){}

    Privileges(Conference conf, AppUser usr){
        this.conf = conf;
        this.usr = usr;
    }

    Privileges(Conference conf, AppUser usr, boolean isAutor, boolean isPCMember, boolean isCoChair, boolean isChair){
        this.conf = conf;
        this.usr = usr;
        this.isAutor  = isAutor;
        this.isPCMember = isPCMember;
        this.isCoChair = isCoChair;
        this.isChair = isChair;
    }
>>>>>>> 81bc439640f04387779552afdd9a099384ff8748

    public int getId() {
        return id;
    }

<<<<<<< HEAD
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
=======
    public void setId(int id) {
        this.id = id;
    }

    public Conference getConf() {
        return conf;
    }

    public void setConf(Conference conf) {
        this.conf = conf;
    }

    public AppUser getUsr() {
        return usr;
    }

    public void setUsr(AppUser usr) {
        this.usr = usr;
    }

    public boolean isAutor() {
        return isAutor;
    }

    public void setAutor(boolean autor) {
        isAutor = autor;
    }

    public boolean isPCMember() {
        return isPCMember;
>>>>>>> 81bc439640f04387779552afdd9a099384ff8748
    }

    public void setPCMember(boolean PCMember) {
        isPCMember = PCMember;
    }

<<<<<<< HEAD
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
=======
    public boolean isChair() {
        return isChair;
    }

    public void setChair(boolean chair) {
        isChair = chair;
    }

    public boolean isCoChair() {
        return isCoChair;
    }

    public void setCoChair(boolean coChair) {
        isCoChair = coChair;
    }


>>>>>>> 81bc439640f04387779552afdd9a099384ff8748
}
