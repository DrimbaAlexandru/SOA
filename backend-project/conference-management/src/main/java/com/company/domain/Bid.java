package com.company.domain;

import javax.persistence.*;

/**
 * Created by Alex on 21.05.2017.
 */

@Table(name = "Bid",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"BidderID", "PaperID"})})
@Entity
public class Bid {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "BidderID", nullable = false)
    private AppUser bidder;

    @ManyToOne
    @JoinColumn(name = "PaperID", nullable = false)
    private Paper paper;

    @ManyToOne
    @JoinColumn(name = "BidStatusID", nullable = false)
    private BidStatus status;

    public Bid(){}

    public BidStatus getStatus() {
        return status;
    }

    public Integer getId() {
        return id;
    }

    public Paper getPaper() {
        return paper;
    }

    public AppUser getBidder() {
        return bidder;
    }

    public void setStatus(BidStatus status) {
        this.status = status;
    }

    public void setBidder(AppUser bidder) {
        this.bidder = bidder;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPaper(Paper paper) {
        this.paper = paper;
    }
}
