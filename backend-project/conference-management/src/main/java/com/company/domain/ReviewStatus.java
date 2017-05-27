package com.company.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Alex on 21.05.2017.
 */

@Table(name = "ReviewStatus")
@Entity
public class ReviewStatus {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @Column(name = "nume", unique = true, nullable = false, length = 20)
    private String nume;

    @OneToMany(mappedBy = "status")
    private Set<Review> reviewsInThisState = new HashSet<>();

    public ReviewStatus(){}

    public String getNume() {
        return nume;
    }

    public int getId() {
        return id;
    }

    public Set<Review> getReviewsInThisState() {
        return reviewsInThisState;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setReviewsInThisState(Set<Review> reviewsInThisState) {
        this.reviewsInThisState = reviewsInThisState;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }
}