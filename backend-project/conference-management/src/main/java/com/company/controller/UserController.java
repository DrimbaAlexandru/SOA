package com.company.controller;

import com.company.controller.DTOs.*;
import com.company.domain.*;
import com.company.service.UserService;
import com.company.utils.ResponseJSON;
import com.company.utils.exception.Exceptional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping( path = "/users" )
public class UserController
{

    private UserService service;

    public UserController( @Autowired UserService service )
    {
        this.service = service;
    }

    private void setCookie( String name, String value, int expiry, HttpServletResponse response )
    {
        Cookie c = new Cookie( name, value );
        c.setMaxAge( expiry );
        c.setPath( "/" );
        response.addCookie( c );
    }

    @RequestMapping( path = "/login", method = RequestMethod.POST )
    public ResponseEntity< ResponseJSON< String > > login( @RequestBody loginRequest body, HttpServletResponse response )
    {
        ResponseJSON< String > resp = new ResponseJSON<>();
        resp.setResp( "Log in response" );
        Exceptional< AppUser > ex = service.getUser( body.getUsername() );
        HttpStatus status = HttpStatus.OK;

        if( ex.isException() )
        {
            resp.addError( ex.getException().getMessage() );
            status = HttpStatus.FORBIDDEN;
        }
        else
        {
            if( !service.login( body.getUsername(), body.getPassword() ) )
            {
                resp.addError( "Incorrect password" );
                status = HttpStatus.FORBIDDEN;
            }
        }

        if( resp.getErrors().size() == 0 )
        {
            setCookie( "username", body.getUsername(), 3600, response );
            setCookie( "password", body.getPassword(), 3600, response );
        }
        else
        {
            setCookie( "username", "ccc", 0, response );
            setCookie( "password", "ccc", 0, response );
        }
        return new ResponseEntity<>( resp, status );
    }

    @RequestMapping( path = "/logout", method = RequestMethod.POST )
    public ResponseEntity< ResponseJSON< String > > logout( HttpServletResponse response )
    {
        ResponseJSON< String > resp = new ResponseJSON<>();
        resp.setResp( "Logged out" );

        setCookie( "username", "", 0, response );
        setCookie( "password", "", 0, response );
        return new ResponseEntity< ResponseJSON< String > >( resp, HttpStatus.OK );
    }

    @RequestMapping( path = "/loggedIn", method = RequestMethod.GET )
    public ResponseEntity< ResponseJSON< UserIdRequest > > handle_loggedIn( @CookieValue( value = "username", defaultValue = "" ) String usernameCookie, @CookieValue( value = "password", defaultValue = "" ) String passwordCookie )
    {
        ResponseJSON< UserIdRequest > resp = new ResponseJSON<>();
        UserIdRequest lir = new UserIdRequest();
        HttpStatus status = HttpStatus.OK;

        if( service.login( usernameCookie, passwordCookie ) )
        {
            Exceptional< AppUser > au = service.getUser( usernameCookie );
            if( au.isException() )
            {
                resp.addError( au.getException().getMessage() );
                status = HttpStatus.FORBIDDEN;
            }
            else
            {
                lir.setUsername( au.getData().getUsername() );
            }
        }
        else
        {
            resp.addError( "User is not logged in or username and password are incorrect" );
            status = HttpStatus.FORBIDDEN;
        }
        resp.setResp( lir );
        return new ResponseEntity<>( resp, status );
    }

    @RequestMapping( path = "/searches", method = RequestMethod.GET )
    public ResponseEntity< ResponseJSON< Iterable< subjectOfInterestResponse > > > handle_getSearchStatus( @CookieValue( value = "username", defaultValue = "" ) String usernameCookie, @CookieValue( value = "password", defaultValue = "" ) String passwordCookie )
    {
        ResponseJSON< Iterable< subjectOfInterestResponse > > resp = new ResponseJSON<>();
        ResponseEntity< ResponseJSON< UserIdRequest > > loginStatus = handle_loggedIn( usernameCookie, passwordCookie );
        HttpStatus status = loginStatus.getStatusCode();
        resp.getErrors().addAll( loginStatus.getBody().getErrors() );
        resp.getWarnings().addAll( loginStatus.getBody().getWarnings() );

        if( resp.getErrors().size() == 0 )
        {
            Exceptional< Iterable< SubjectOfInterest > > searchResults = service.getSubjectsOfInterest( usernameCookie );
            if( searchResults.isException() )
            {
                resp.addError( searchResults.getException().getMessage() );
                status = HttpStatus.NOT_FOUND;
            }
            else
            {
                List< subjectOfInterestResponse > response = new ArrayList<>();
                for( SubjectOfInterest soi : searchResults.getData() )
                {
                    subjectOfInterestResponse item = new subjectOfInterestResponse();
                    item.setNewlyReported( soi.getDisplayCountdown() > 0 );
                    item.setResultsCount( soi.getResultsCount() );
                    item.setSearchLink( soi.getResultsPageLink() );
                    item.setSearchString( soi.getSearchString() );
                    response.add( item );
                }
                resp.setResp( response );
            }
        }
        return new ResponseEntity<>( resp, status );
    }

    @RequestMapping( path = "", method = RequestMethod.POST )
    public ResponseEntity< ResponseJSON< String > > handle_create_user( @RequestBody updateUserRequest body )
    {
        ResponseJSON< String > resp = new ResponseJSON<>( "" );
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Exceptional< AppUser > au = service.getUser( body.getUsername() );

        if( au.isOK() )
        {
            resp.addError( "An user with this username already exists" );
        }
        else
        {
            au = service.addUser( new AppUser( body.getUsername(), body.getEmail(), body.getPassword(), body.getDiscogsToken() ) );
            if( au.isException() )
            {
                resp.addError( "Unexpected error. The given email may be already be used by another user" );
            }
            else
            {
                status = HttpStatus.OK;
            }
        }
        return new ResponseEntity< ResponseJSON< String > >( resp, status );

    }

    @RequestMapping( path = "", method = RequestMethod.PUT )
    public ResponseEntity< ResponseJSON< String > > handle_update_user( @RequestBody updateUserRequest body, @CookieValue( value = "username", defaultValue = "" ) String usernameCookie, @CookieValue( value = "password", defaultValue = "" ) String passwordCookie, HttpServletResponse response )
    {
        ResponseJSON< String > resp = new ResponseJSON<>( "" );
        ResponseEntity< ResponseJSON< UserIdRequest > > loginStatus = handle_loggedIn( usernameCookie, passwordCookie );
        HttpStatus status = loginStatus.getStatusCode();
        resp.getErrors().addAll( loginStatus.getBody().getErrors() );
        resp.getWarnings().addAll( loginStatus.getBody().getWarnings() );

        if( resp.getErrors().size() == 0 )
        {
            Exceptional< AppUser > au = service.getUser( usernameCookie );
            if( au.isException() )
            {
                resp.addError( au.getException().getMessage() );
                status = HttpStatus.NOT_FOUND;
            }
            else
            {
                AppUser u = au.getData();
                au = service.updateUser( u.getUsername(), new AppUser( u.getUsername(), u.getEmail(), u.getPassword(), u.getDiscogsToken() ) );
                if( au.isException() )
                {
                    resp.addError( au.getException().getMessage() );
                    status = HttpStatus.INTERNAL_SERVER_ERROR;
                }
                else
                {
                    if( body.getUsername() != null )
                    {
                        setCookie( "username", body.getUsername(), 3600, response );
                    }
                    if( body.getPassword() != null )
                    {
                        setCookie( "password", body.getPassword(), 3600, response );
                    }
                }
            }
        }

        return new ResponseEntity<>( resp, status );
    }

    @RequestMapping( path = "/searches", method = RequestMethod.POST )
    public ResponseEntity< ResponseJSON< subjectOfInterestResponse > > handle_postSearchQuery( @CookieValue( value = "username", defaultValue = "" ) String usernameCookie, @CookieValue( value = "password", defaultValue = "" ) String passwordCookie, @RequestBody subjectOfInterestRequest body )
    {
        ResponseJSON< subjectOfInterestResponse > resp = new ResponseJSON<>();
        ResponseEntity< ResponseJSON< UserIdRequest > > loginStatus = handle_loggedIn( usernameCookie, passwordCookie );
        HttpStatus status = loginStatus.getStatusCode();
        resp.getErrors().addAll( loginStatus.getBody().getErrors() );
        resp.getWarnings().addAll( loginStatus.getBody().getWarnings() );

        if( resp.getErrors().size() == 0 )
        {
            Exceptional< SubjectOfInterest > Esoi = service.addSubjectOfInterest( usernameCookie, body.getSearchString() );
            if( Esoi.isOK() )
            {
                SubjectOfInterest soi = Esoi.getData();
                subjectOfInterestResponse item = new subjectOfInterestResponse();
                item.setNewlyReported( soi.getDisplayCountdown() > 0 );
                item.setResultsCount( soi.getResultsCount() );
                item.setSearchLink( soi.getResultsPageLink() );
                item.setSearchString( soi.getSearchString() );
                resp.setResp( item );
            }
            else
            {
                resp.addError( Esoi.getException().getMessage() );
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
        return new ResponseEntity<>( resp, HttpStatus.OK );

    }

}

