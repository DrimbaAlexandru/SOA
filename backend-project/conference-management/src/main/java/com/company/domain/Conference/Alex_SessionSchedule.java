package com.company.domain.Conference;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Alex on 21.05.2017.
 */
@Table(name = "SessionSchedule")
@Entity
public class Alex_SessionSchedule {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @OneToOne
    @JoinColumn(name = "PaperID")
    private Paper paper;

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

    public Alex_SessionSchedule(){}

}