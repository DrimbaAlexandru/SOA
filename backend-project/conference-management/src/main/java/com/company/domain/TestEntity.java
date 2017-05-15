package com.company.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by AlexandruD on 15-May-17.
 */
@Table(name = "TestEntity")
@Entity
public class TestEntity {

    private Integer id;
    private String string;

    private Set<TestEnttiyOTMChild> children;

    public TestEntity() {
        children = new HashSet<>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer nId) {
        this.id = nId;
    }

    @Column(name="string")
    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "test_entity_id")
    public Set<TestEnttiyOTMChild> getChildren() {
        return children;
    }

    public void setChildren(
            Set<TestEnttiyOTMChild> children) {
        this.children = children;
    }
}
