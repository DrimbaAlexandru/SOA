package com.company.domain;

import javax.persistence.*;

/**
 * Created by Alex on 21.05.2017.
 */
@Table(name = "Review",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"ReviewerID", "PaperID"})})
@Entity
public class Review {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @ManyToOne()
    @JoinColumn(name = "ReviewerID", nullable = false)
    private AppUser reviewer;

    @ManyToOne()
    @JoinColumn(name = "PaperID", nullable = false)
    private Paper paper;

    @ManyToOne()
    @JoinColumn(name = "ReviewStatusID", nullable = false)
    private ReviewStatus status;

    public Review(){}

    public ReviewStatus getStatus() {
        return status;
    }

    public Integer getId() {
        return id;
    }

    public Paper getPaper() {
        return paper;
    }

    public AppUser getReviewer() {
        return reviewer;
    }

    public void setStatus(ReviewStatus status) {
        this.status = status;
    }

    public void setReviewer(AppUser reviewer) {
        this.reviewer = reviewer;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPaper(Paper paper) {
        this.paper = paper;
    }
}
