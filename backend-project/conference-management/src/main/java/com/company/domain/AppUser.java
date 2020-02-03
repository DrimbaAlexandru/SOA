package com.company.domain;

import javax.persistence.*;
import java.util.Set;

@Table(name = "AppUser")
@Entity
public class AppUser {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY )
    @Column(name = "id")
    private Integer id;

    @Column(name = "username",unique = true, nullable = false, length = 64)
    private String username;

    @Column(name = "email",unique = true, nullable = false, length = 64)
    private String email;

    @Column(name = "password", nullable = false,length = 64)
    private String password;

    @Column(name = "discogsToken", nullable = false,length = 64)
    private String discogsToken;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private Set<SubjectOfInterest> subjectOfInterest;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getDiscogsToken() {
        return discogsToken;
    }

    public Set<SubjectOfInterest> getSubjectOfInterest() {
        return subjectOfInterest;
    }

    public void setDiscogsToken(String discogsToken) {
        this.discogsToken = discogsToken;
    }

    public void setSubjectOfInterest(Set<SubjectOfInterest> subjectOfInterest) {
        this.subjectOfInterest = subjectOfInterest;
    }

    public AppUser(){}

    public AppUser(String username, String email, String password, String discogsToken) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.discogsToken = discogsToken;
    }

}