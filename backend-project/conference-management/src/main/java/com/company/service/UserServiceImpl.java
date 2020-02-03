package com.company.service;

import com.company.domain.*;
import com.company.repository.InterestRepository;
import com.company.repository.UserRepository;
import com.company.service.RemoteServices.RemoteResultsStalker;
import com.company.service.RemoteServices.SearchResults;
import com.company.utils.exception.Exceptional;
import com.company.utils.updater.InterestsGettersAndSetters;
import com.company.utils.updater.Updater;
import com.company.utils.updater.UsersGettersAndSetters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Created by AlexandruD on 02-Jun-17.
 */
@Component
public class UserServiceImpl implements UserService
{

    private UserRepository userRepository;
    private InterestRepository interestRepository;
    private PasswordEncoder encoder;
    private UsersGettersAndSetters usersGettersAndSetters;
    private InterestsGettersAndSetters interestsGettersAndSetters;
    private Updater updater;
    private RemoteResultsStalker resultsStalker;

    public UserServiceImpl( @Autowired UserRepository userRepository, @Autowired InterestRepository interestRepository, @Autowired PasswordEncoder encoder, @Autowired UsersGettersAndSetters usersGettersAndSetters, @Autowired InterestsGettersAndSetters interestsGettersAndSetters, @Autowired RemoteResultsStalker resultsStalker )
    {
        this.userRepository = userRepository;
        this.interestRepository = interestRepository;
        this.encoder = encoder;
        this.usersGettersAndSetters = usersGettersAndSetters;
        this.interestsGettersAndSetters = interestsGettersAndSetters;
        this.resultsStalker = resultsStalker;

        this.updater = new Updater();
    }

    @Override
    public boolean login( String username, String password )
    {
        AppUser usr = userRepository.findByUsername( username );
        return usr != null && encoder.matches( password, usr.getPassword() );
    }

    @Override
    public Exceptional< AppUser > getUser( String username )
    {
        AppUser usr = userRepository.findByUsername( username );

        return ( usr == null ) ? Exceptional.Error( new Exception( "Username not found" ) ) : Exceptional.OK( usr );
    }

    @Override
    public Exceptional< AppUser > addUser( AppUser u )
    {
        // Encrypt password
        u.setPassword( encoder.encode( u.getPassword() ) );
        try
        {
            return Exceptional.OK( userRepository.save( u ) );
        }
        catch( Exception e )
        {
            return Exceptional.Error( e );
        }
    }

    @Override
    public Exceptional< AppUser > updateUser( String username, AppUser u )
    {
        AppUser au = userRepository.findByUsername( username );
        if( u.getPassword() != null )
        {
            u.setPassword( encoder.encode( u.getPassword() ) );
        }
        if( au == null )
        {
            return Exceptional.Error( new Exception( "Username not found" ) );
        }
        try
        {
            updater.update( au, u, usersGettersAndSetters.getGettersAndSetters() );
            this.userRepository.save( au );
            return Exceptional.OK( au );
        }
        catch( Exception e )
        {
            return Exceptional.Error( e );
        }
    }

    @Override
    public Exceptional< Iterable< SubjectOfInterest > > getSubjectsOfInterest( String username )
    {
        AppUser owner = userRepository.findByUsername( username );
        if( owner == null )
        {
            return Exceptional.Error( new Exception( "Username not found" ) );
        }
        return Exceptional.OK( owner.getSubjectOfInterest() );
    }

    @Override
    public Exceptional< SubjectOfInterest > addSubjectOfInterest( String username, String searchString )
    {
        AppUser owner = userRepository.findByUsername( username );
        if( owner == null )
        {
            return Exceptional.Error( new Exception( "Username not found" ) );
        }

        SubjectOfInterest subject = interestRepository.findBySearchStringAndOwnerId( searchString, owner.getId() );

        if( subject != null )
        {
            return Exceptional.Error( new Exception( "Subject already exists" ) );
        }
        subject = new SubjectOfInterest();
        subject.setDisplayCountdown( 2 );
        subject.setOwner( owner );
        subject.setSearchString( searchString );
        subject.setDisplayCountdown( 1 );
        subject.setResultsCount( 0 );
        subject.setResultsPageLink( "" );

        try
        {
            syncResults( searchString, owner, subject );
            subject = interestRepository.save( subject );
            return Exceptional.OK( subject );
        }
        catch( Exception e )
        {
            return Exceptional.Error( e );
        }
    }

    @Override
    public Exceptional< SubjectOfInterest > updateSubjectOfInterest( String username, Integer SoIId )
    {
        SubjectOfInterest subject = interestRepository.findOne( SoIId );
        SubjectOfInterest newSubject = new SubjectOfInterest();
        boolean updated = false;

        if( subject == null )
        {
            return Exceptional.Error( new Exception( "Item not found" ) );
        }
        if( !subject.getOwner().getUsername().equals( username ) )
        {
            return Exceptional.Error( new Exception( "You are not the owner of the selected ID" ) );
        }
        newSubject.setDisplayCountdown( subject.getDisplayCountdown() );
        if( newSubject.getDisplayCountdown() > 0 )
        {
            newSubject.setDisplayCountdown( newSubject.getDisplayCountdown() - 1 );
            updated = true;
        }

        try
        {
            // Discogs API logic goes here
            updated = updated | syncResults( subject.getSearchString(), subject.getOwner(), subject );

            if( updated )
            {
                updater.update( subject, newSubject, interestsGettersAndSetters.getGettersAndSetters() );
                interestRepository.save( subject );
            }
            return Exceptional.OK( subject );
        }
        catch( Exception e )
        {
            return Exceptional.Error( e );
        }
    }

    private boolean syncResults( String searchString, AppUser user, SubjectOfInterest soi ) throws Exception
    {
        Exceptional< SearchResults > results = resultsStalker.findResults( "", user.getDiscogsToken(), searchString );
        if( results.isOK() )
        {
            SearchResults sr = results.getData();
            if( !soi.getResultsCount().equals( sr.getResultsCount() ) )
            {
                soi.setResultsPageLink( sr.getQueryLink() );
                soi.setResultsCount( sr.getResultsCount() );
                soi.setDisplayCountdown( 3 );
                return true;
            }
            return false;
        }
        throw results.getException();
    }
}