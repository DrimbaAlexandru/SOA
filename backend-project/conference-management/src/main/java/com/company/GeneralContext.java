package com.company;

import com.company.service.RemoteServices.DiscogsResultsStalker;
import com.company.service.RemoteServices.RemoteResultsStalker;

import com.company.utils.updater.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class GeneralContext
{

    @Bean
    public PasswordEncoder getPasswordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UsersGettersAndSetters getUsersGettersAndSetters()
    {
        return new UsersGettersAndSetters();
    }

    @Bean
    public InterestsGettersAndSetters getInterestsGettersAndSetters()
    {
        return new InterestsGettersAndSetters();
    }

    @Bean
    public RemoteResultsStalker getResultsService( Environment env )
    {
        return new DiscogsResultsStalker( env.getProperty( "discogs.accept_header" ),
                                          env.getProperty( "discogs.accept_header" ) );
    }
}
