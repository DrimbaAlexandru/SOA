package com.company.service.RemoteServices;

import com.company.utils.exception.Exceptional;

public interface RemoteResultsStalker
{
    Exceptional<SearchResults> findResults( String username, String accessToken, String searchString );
}
