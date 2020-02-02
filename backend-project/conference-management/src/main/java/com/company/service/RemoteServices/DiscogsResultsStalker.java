package com.company.service.RemoteServices;

import com.company.utils.exception.Exceptional;
import org.hibernate.cfg.NotYetImplementedException;

public class DiscogsResultsStalker implements RemoteResultsStalker
{
    private String UserAgent;
    private String AcceptHeader;

    public DiscogsResultsStalker( String userAgent, String acceptHeader )
    {
        this.AcceptHeader=acceptHeader;
        this.UserAgent=userAgent;
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
    public Exceptional<SearchResults> findResults( String username, String accessToken, String searchString )
    {
        return Exceptional.Error( new NotYetImplementedException( "Not yet implemented" ) );
    }
}
