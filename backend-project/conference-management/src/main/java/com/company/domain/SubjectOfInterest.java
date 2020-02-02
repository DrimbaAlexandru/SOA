package com.company.domain;

import javax.persistence.*;

@Table(name = "SubjectOfInterest",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"ownerID", "searchString"})})
@Entity
public class SubjectOfInterest {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY )
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ownerID", nullable = false)
    private AppUser owner;

    @Column(name = "searchString", nullable = false)
    private String searchString;

    @Column(name = "displayCountdown", nullable = false)
    private Integer displayCountdown;

    public SubjectOfInterest(){}

    public AppUser getOwner() {
        return owner;
    }

    public Integer getDisplayCountdown() {
        return displayCountdown;
    }

    public Integer getId() {
        return id;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setDisplayCountdown(Integer displayCountdown) {
        this.displayCountdown = displayCountdown;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setOwner(AppUser owner) {
        this.owner = owner;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }
}
