package com.company.controller.DTOs;

import com.company.domain.SubjectOfInterest;

public class subjectOfInterestResponse
{
    private String searchString;
    private Integer resultsCount;
    private Boolean newlyReported;
    private String searchLink;

    public subjectOfInterestResponse(){}

    public subjectOfInterestResponse( String searchString, Integer resultsCount, Boolean newlyReported, String searchLink )
    {
        this.newlyReported = newlyReported;
        this.resultsCount = resultsCount;
        this.searchLink = searchLink;
        this.searchString = searchString;
    }

    public String getSearchString() {
        return searchString;
    }

    public Boolean getNewlyReported() {
        return newlyReported;
    }

    public Integer getResultsCount() {
        return resultsCount;
    }

    public String getSearchLink() {
        return searchLink;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public void setNewlyReported(Boolean newlyReported) {
        this.newlyReported = newlyReported;
    }

    public void setResultsCount(Integer resultsCount) {
        this.resultsCount = resultsCount;
    }

    public void setSearchLink(String searchLink) {
        this.searchLink = searchLink;
    }
}
