package com.company.domain.Conference;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Alex on 21.05.2017.
 */

@Table(name = "AppUser")
@Entity
public class AppUser {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @ManyToMany
    @JoinTable(name="SubmittedPapers")
    private Set<Paper> submittedPapers=new HashSet<>();

    @OneToMany(mappedBy = "speaker")
    private Set<SessionSchedule> presentationsIsSpeakerFor;

    @ManyToMany(mappedBy = "listeners")
    private Set<SessionSchedule> presentationsIsListenerFor;

    @Column(name = "username", unique = true, nullable = false, length = 32)
    private String username;

    @Column(name = "nume", unique = false, nullable = false, length = 128)
    private String nume;

    @Column(name = "affiliation", unique = false, nullable = false, length = 64)
    private String affiliation;

    @Column(name = "email", unique = true, nullable = false, length = 64)
    private String email;

    @Column(name = "webpage", unique = false, nullable = true, length = 64)
    private String webpage;

    @Column(name = "password", unique = true, nullable = false, length = 64)
    private String password;

    @Column(name = "isSuperUser", unique = false, nullable = false)
    private boolean isSuperUser;

    @Column(name = "isComiteeMember", unique = false, nullable = false)
    private boolean isComiteeMember;

    @OneToMany(mappedBy = "user")
    private Set<Privileges> privileges;

    public String getNume() {
        return nume;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getWebpage() {
        return webpage;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public void setComiteeMember(boolean comiteeMember) {
        isComiteeMember = comiteeMember;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setSuperUser(boolean superUser) {
        isSuperUser = superUser;
    }

    public void setWebpage(String webpage) {
        this.webpage = webpage;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setSubmittedPapers(Set<Paper> submittedPapers) {
        this.submittedPapers = submittedPapers;
    }

    public Set<Paper> getSubmittedPapers() {
        return submittedPapers;
    }

    public void setPrivileges(Set<Privileges> privileges) {
        this.privileges = privileges;
    }

    public Set<Privileges> getPrivileges() {
        return privileges;
    }

    public AppUser(){}
}
