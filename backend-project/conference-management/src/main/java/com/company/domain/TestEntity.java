package com.company.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by AlexandruD on 15-May-17.
 */
@Table(name = "Parinte")
@Entity
public class TestEntity {

    private Integer id;
    private String string;

    private Set<TestEnttiyOTMChild> children1;
    private Set<TestEnttiyOTMChild> children2;

    public TestEntity() {
        children1 = new HashSet<>();
        children2=new HashSet<>();
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
    @JoinColumn(name = "parinte1_Id")
    public Set<TestEnttiyOTMChild> getChildren1() {
        return children1;
    }

    public void setChildren1(
            Set<TestEnttiyOTMChild> children) {
        this.children1 = children;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "parinte2_Id")
    public Set<TestEnttiyOTMChild> getChildren2() {
        return children2;
    }

    public void setChildren2(
            Set<TestEnttiyOTMChild> children) {
        this.children2 = children;
    }
}
