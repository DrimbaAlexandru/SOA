package com.company.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Alex on 21.05.2017.
 */

@Table(name = "Paper")
@Entity
public class Paper {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY )
    @Column(name = "id")
    private Integer id;

    @Column(name = "nume",unique = false, nullable = false, length = 256)
    private String nume;

    @Enumerated(value = EnumType.STRING)
    private PaperStatus status;

    @OneToOne(mappedBy = "paper")
//!!!!
    private SessionSchedule schedule;

    @ManyToMany(mappedBy="submittedPapers", cascade = CascadeType.ALL)
//!!!!
    private Set<AppUser> authors;

    @ManyToMany(mappedBy="assignedForReview", cascade = CascadeType.ALL)
//!!!!
    private Set<AppUser> reviewers;

    @OneToMany(mappedBy = "paper", cascade = CascadeType.ALL,orphanRemoval=true)
    private Set<Review> reviews;

    @OneToMany(mappedBy = "paper", cascade = CascadeType.ALL)
    private Set<Bid> bids;

    @OneToMany(mappedBy = "paperIsAbstractFor", cascade = CascadeType.ALL)
    private Set<UploadedFile> abstracts;

    @OneToMany(mappedBy = "paperIsFullPaperFor", cascade = CascadeType.ALL)
    private Set<UploadedFile> fullPapers;

    @OneToOne(mappedBy = "paperIsPresentationFor", cascade = CascadeType.ALL)
    private UploadedFile presentation = null;

    public Paper(){}

    public Paper(String _nume, PaperStatus _status, Set<AppUser> _authors)
    {
        nume=_nume;
        status=_status;
        authors=_authors;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public void setAbstracts(Set<UploadedFile> abstracts) {
        this.abstracts = abstracts;
    }

    public void setAuthors(Set<AppUser> authors) {
        this.authors = authors;
    }

    public void setBids(Set<Bid> bids) {
        this.bids = bids;
    }

    public void setFullPapers(Set<UploadedFile> fullPapers) {
        this.fullPapers = fullPapers;
    }

    public void setPresentation(UploadedFile presentation) {
        this.presentation = presentation;
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }

    public void setStatus(PaperStatus status) {
        this.status = status;
    }

    public void setSchedule(SessionSchedule schedule) {
        this.schedule = schedule;
    }

    public String getNume() {
        return nume;
    }

    public Integer getId() {
        return id;
    }

    public PaperStatus getStatus() {
        return status;
    }

    public Set<Bid> getBids() {
        return bids;
    }

    public Set<Review> getReviews() {
        return reviews;
    }

    public Set<UploadedFile> getAbstracts() {
        return abstracts;
    }


    public Set<UploadedFile> getFullPapers() {
        return fullPapers;
    }

    public UploadedFile getPresentation() {
        return presentation;
    }


    public SessionSchedule getSchedule() {
        return schedule;
    }


    public Set<AppUser> getAuthors() {
        return authors;
    }

    public Set<AppUser> getReviewers() {
        return reviewers;
    }

    public void setReviewers(Set<AppUser> reviewers) {
        this.reviewers = reviewers;
    }
}