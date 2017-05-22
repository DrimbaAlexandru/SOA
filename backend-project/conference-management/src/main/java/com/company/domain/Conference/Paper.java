package com.company.domain.Conference;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Alex on 21.05.2017.
 */

@Table(name = "Paper")
@Entity
public class Paper {

    @Id @GeneratedValue
    @Column(name = "id")
    private int id;

    @Column(name = "nume",unique = false, nullable = false, length = 256)
    private String nume;

    @ManyToOne()
    @JoinColumn(name = "statusID", nullable = false)
    private PaperStatus status;

    @OneToOne(mappedBy = "paper")
//!!!!
    //@JoinColumn(name = "PaperID")
    private Alex_SessionSchedule schedule;

    @ManyToMany(mappedBy="submittedPapers")
//!!!!
    //@JoinTable(name="SubmittedPapers")
    private Set<Alex_AppUser> authors= new HashSet<>(0);

    @OneToMany(mappedBy = "paper")
    private Set<Review> reviews= new HashSet<>(0);

    @OneToMany(mappedBy = "paper")
    private Set<Bid> bids= new HashSet<>(0);

    @OneToMany(mappedBy = "paperIsAbstractFor")
    private Set<UploadedFile> abstracts= new HashSet<>(0);

    @OneToMany(mappedBy = "paperIsFullPaperFor")
    private Set<UploadedFile> fullPapers= new HashSet<>(0);

    @OneToOne(mappedBy = "paperIsPresentationFor")
    private UploadedFile presentation = null;

    public Paper(){}

    public Paper(String _nume, PaperStatus _status, Set<Alex_AppUser> _authors)
    {
        nume=_nume;
        status=_status;
        authors=_authors;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public void setAbstracts(Set<UploadedFile> abstracts) {
        this.abstracts = abstracts;
    }

    public void setAuthors(Set<Alex_AppUser> authors) {
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

    public void setSchedule(Alex_SessionSchedule schedule) {
        this.schedule = schedule;
    }

    public String getNume() {
        return nume;
    }

    public int getId() {
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


    public Alex_SessionSchedule getSchedule() {
        return schedule;
    }


    public Set<Alex_AppUser> getAuthors() {
        return authors;
    }
}
