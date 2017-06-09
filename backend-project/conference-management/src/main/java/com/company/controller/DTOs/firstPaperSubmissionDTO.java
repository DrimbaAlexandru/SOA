package com.company.controller.DTOs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 08.06.2017.
 */
public class firstPaperSubmissionDTO
{
    private Integer id;
    private String name;
    private List<String> subjects=new ArrayList<>();
    private List<String> keywords=new ArrayList<>();
    private List<String> authors;
    private Integer conferenceId;

    public firstPaperSubmissionDTO(){}

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }

    public void setConferenceId(Integer conferenceId) {
        this.conferenceId = conferenceId;
    }

    public Integer getConferenceId() {
        return conferenceId;
    }
}