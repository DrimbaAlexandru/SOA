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

    @Column(name = "username",unique = true, nullable = false)
    private String usrName;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "affiliation")
    private String affiliation;

    @Column(name = "emali",unique = true, nullable = false)
    private String email;

    @Column(name = "webpage")
    private String webpage;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "isSuperUser")
    private boolean isSuperUser;

    @Column(name = "isCometeeMember")
    private boolean isCometeeMember;

    @ManyToMany
    @JoinTable(name="SubmittedPapers")
    private Set<Paper> submittedPapers=new HashSet<>();

    @OneToMany(mappedBy = "user_ID")
    private Set<Privileges> privileges = new HashSet<>();

    @OneToOne(mappedBy = "sesionChair")

    private int sesionChair;

    @ManyToMany(mappedBy = "Listeners")
    private Set<Sesion> sesions = new HashSet<>();


    public int getSesionChair() {
        return sesionChair;
    }

    public void setSesionChair(int sesionChair) {
        this.sesionChair = sesionChair;
    }

    public Set<Sesion> getSesions() {
        return sesions;
    }

    public void setSesions(Set<Sesion> sesions) {
        this.sesions = sesions;
    }


    public String getUsrName() {
        return usrName;
    }

    public void setUsrName(String usrName) {
        this.usrName = usrName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebpage() {
        return webpage;
    }

    public void setWebpage(String webpage) {
        this.webpage = webpage;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isSuperUser() {
        return isSuperUser;
    }

    public void setSuperUser(boolean superUser) {
        isSuperUser = superUser;
    }

    public boolean isCometeeMember() {
        return isCometeeMember;
    }

    public void setCometeeMember(boolean cometeeMember) {
        isCometeeMember = cometeeMember;
    }

    public Set<Privileges> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(Set<Privileges> privileges) {
        this.privileges = privileges;
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

    public AppUser(){}

    public AppUser(String usrName, String name, String affiliation, String email, String webpage, String password) {
        this.usrName = usrName;
        this.name = name;
        this.affiliation = affiliation;
        this.email = email;
        this.webpage = webpage;
        this.password = password;
    }

    public AppUser(String usrName, String name, String affiliation, String email,
                   String webpage, String password, boolean isSuperUser, boolean isCometeeMember) {
        this.usrName = usrName;
        this.name = name;
        this.affiliation = affiliation;
        this.email = email;
        this.webpage = webpage;
        this.password = password;
        this.isSuperUser = isSuperUser;
        this.isCometeeMember = isCometeeMember;
    }
}
