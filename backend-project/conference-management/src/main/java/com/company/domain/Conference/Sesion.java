package com.company.domain.Conference;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Crisitan on 5/22/2017.
 */

@Table(name = "Sesion")
@Entity
public class Sesion {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @Column(name = "holdingTime", nullable = false)
    private long holdingTime;

    @ManyToOne
    @JoinColumn(name = "conf_ID", nullable = false)
    private int conf_ID;

    @ManyToMany
    @JoinTable(name = "Listeners")
    private Set<AppUser> listeners = new HashSet<>();

    @OneToOne
    @JoinTable(name = "sesionChair")
    private String sesionChair;

    public  Sesion(){}

    public Sesion(long holdingTime, int conf_ID) {
        this.holdingTime = holdingTime;
        this.conf_ID = conf_ID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getHoldingTime() {
        return holdingTime;
    }

    public void setHoldingTime(long holdingTime) {
        this.holdingTime = holdingTime;
    }

    public int getConf_ID() {
        return conf_ID;
    }

    public void setConf_ID(int conf_ID) {
        this.conf_ID = conf_ID;
    }

    public Set<AppUser> getListeners() {
        return listeners;
    }

    public void setListeners(Set<AppUser> listeners) {
        this.listeners = listeners;
    }

    public String getSesionChair() {
        return sesionChair;
    }

    public void setSesionChair(String sesionChair) {
        this.sesionChair = sesionChair;
    }


}
