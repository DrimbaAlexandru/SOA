package com.company.domain;

/**
 * Created by Alex on 21.05.2017.
 */

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Table(name = "PaperStatus")
@Entity
public class PaperStatus {
    @Id @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @Column(name = "nume",unique = true, nullable = false, length = 20)
    private String nume;

    @OneToMany(mappedBy = "status")
    private Set<Paper> papersInThisState = new HashSet<>(0);

    public PaperStatus(){}

    public PaperStatus(String nume)
    {
        this.nume=nume;
    }

    public Integer getId() {
        return id;
    }

    public String getNume() {
        return nume;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public Set<Paper> getPapersInThisState() {
        return papersInThisState;
    }

    public void setPapersInThisState(Set<Paper> papersInThisState) {
        this.papersInThisState = papersInThisState;
    }
}
