package com.company.domain.Conference;

import javax.persistence.*;

/**
 * Created by Crisitan on 5/22/2017.
 */

@Table(name = "Privileges")
@Entity
public class Privileges {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @ManyToOne
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

    public int getId() {
        return id;
    }

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
    }

    public void setPCMember(boolean PCMember) {
        isPCMember = PCMember;
    }

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


}
