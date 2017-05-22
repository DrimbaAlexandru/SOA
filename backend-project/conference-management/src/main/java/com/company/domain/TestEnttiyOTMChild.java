package com.company.domain;

import javax.persistence.*;

/**
 * Created by AlexandruD on 15-May-17.
 */
@Entity
@Table(name = "Copil")
public class TestEnttiyOTMChild {

    private Integer id;

    private TestEntity parinte1;

    private TestEntity parinte2;

    public TestEnttiyOTMChild() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parinte1_id", nullable = false)
    public TestEntity getParinte1() {
        return parinte1;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parinte2_id", nullable = false)
    public TestEntity getParinte2() {
        return parinte2;
    }

    public void setParinte2(TestEntity parinte2) {
        this.parinte2 = parinte2;
    }

    public void setParinte1(TestEntity parinte1) {
        this.parinte1 = parinte1;
    }
}
