package com.company.service.RemoteServices;

public class SearchResults
{
    private String queryLink;
    private Integer resultsCount;

    public Integer getResultsCount()
    {
        return resultsCount;
    }

    public String getQueryLink()
    {
        return queryLink;
    }

    public void setResultsCount( Integer resultsCount )
    {
        this.resultsCount = resultsCount;
    }

    public void setQueryLink( String queryLink )
    {
        this.queryLink = queryLink;
    }
}
