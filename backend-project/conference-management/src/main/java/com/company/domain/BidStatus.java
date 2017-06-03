package com.company.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Alex on 21.05.2017.
 */

@Table(name = "BidStatus")
@Entity
public class BidStatus {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @Column(name = "nume", unique = true, nullable = false, length = 20)
    private String nume;

    @OneToMany(mappedBy = "status")
    private Set<Bid> bidsInThisState = new HashSet<>();

    public BidStatus(){}

    public String getNume() {
        return nume;
    }

    public Integer getId() {
        return id;
    }


    public Set<Bid> getBidsInThisState() {
        return bidsInThisState;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setBidsInThisState(Set<Bid> bidsInThisState) {
        this.bidsInThisState = bidsInThisState;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }
}