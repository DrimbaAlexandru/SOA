package com.company.domain.Conference;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Alex on 21.05.2017.
 */

@Table(name = "AppUser")
@Entity
public class Alex_AppUser {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @ManyToMany
    @JoinTable(name="SubmittedPapers")
    private Set<Paper> submittedPapers=new HashSet<>();

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

    public Alex_AppUser(){}

}
