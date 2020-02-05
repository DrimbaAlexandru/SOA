package com.company.service.RemoteServices;

import com.company.controller.DTOs.RemoteDTOs.DiscogsSearchResponse;
import com.company.utils.exception.Exceptional;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class DiscogsResultsStalker implements RemoteResultsStalker
{
    private String UserAgent;
    private String AcceptHeader;
    private final String serverAPI = "https://api.discogs.com/";
    private final String server = "https://discogs.com/";

    private RestTemplate rest;

    public DiscogsResultsStalker( String userAgent, String acceptHeader )
    {
        this.AcceptHeader = acceptHeader;
        this.UserAgent = userAgent;
        this.rest = new RestTemplate();
    }

    public void setAcceptHeader( String acceptHeader )
    {
        AcceptHeader = acceptHeader;
    }

    public void setUserAgent( String userAgent )
    {
        UserAgent = userAgent;
    }

    public String getAcceptHeader()
    {
        return AcceptHeader;
    }

    public String getUserAgent()
    {
        return UserAgent;
    }

    @Override
    public Exceptional< SearchResults > findResults( String username, String accessToken, String searchString )
    {
        String query = serverAPI + "/database/search?q={searchString}";
        String searchLink = server + "search/?q=" + searchString + "&type=all";
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status;
        DiscogsSearchResponse response;

        headers.add( "Content-Type", "application/json" );
        headers.add( "Accept", this.AcceptHeader );
        headers.add( "User-Agent", this.UserAgent );
        headers.add( "Authorization", "Discogs token=" + accessToken );

        try
        {
            HttpEntity< String > requestEntity = new HttpEntity< String >( "", headers );
            ResponseEntity< DiscogsSearchResponse > responseEntity = rest.exchange( query, HttpMethod.GET, requestEntity, DiscogsSearchResponse.class, searchString );
            status = responseEntity.getStatusCode();
            response = responseEntity.getBody();

            if( status != HttpStatus.OK )
            {
                throw new Exception( "Request returned status " + status );
            }

            //return Exceptional.Error( new NotYetImplementedException( "Not yet implemented" ) );
            SearchResults results = new SearchResults();
            results.setQueryLink( searchLink );
            results.setResultsCount( response.pagination.items );
            return Exceptional.OK( results );
        }
        catch( Exception e )
        {
            return Exceptional.Error( e );
        }
    }
}
