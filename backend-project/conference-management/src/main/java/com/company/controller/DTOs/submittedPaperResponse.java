package com.company.controller.DTOs;

import com.company.domain.Paper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Alex on 07.06.2017.
 */
public class submittedPaperResponse
{
    private int id;
    private String name;
    private List<String> subjects=new ArrayList<>();
    private List<String> keywords=new ArrayList<>();
    private List<String> authors;

    public submittedPaperResponse(Paper p)
    {
        id=p.getId();
        name=p.getNume();
        authors=p.getAuthors().stream().map((u)->{return u.getUsername();}).collect(Collectors.toList());
    }
    public String getName() {
        return name;
    }

    public int getId() {
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
}
