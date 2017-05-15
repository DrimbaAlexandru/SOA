package com.company.domain;

import javax.persistence.*;

/**
 * Created by AlexandruD on 15-May-17.
 */
@Entity
@Table(name = "TestEntityOTMChild")
public class TestEnttiyOTMChild {

    private Integer id;

    private TestEntity testEntity;

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
    @JoinColumn(name = "testentity_id", nullable = false)
    public TestEntity getTestEntity() {
        return testEntity;
    }

    public void setTestEntity(TestEntity test) {
        this.testEntity = test;
    }

}
