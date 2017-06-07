package com.company;

import com.company.utils.RemoteFileManager;
import com.company.utils.dropbox.DropboxManagerRemote;
import com.company.utils.updater.ConferencesGettersAndSetters;
import com.company.utils.updater.PapersGettersAndSetters;
import com.company.utils.updater.PrivilegesGettersAndSetters;
import com.company.utils.updater.UsersGettersAndSetters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by AlexandruD on 15-May-17.
 */
@Configuration
public class GeneralContext {

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UsersGettersAndSetters getUsersGettersAndSetters() {
        return new UsersGettersAndSetters();
    }

    @Bean
    public PrivilegesGettersAndSetters getPrivilegesGettersAndSetters() {
        return new PrivilegesGettersAndSetters();
    }

    @Bean
    public PapersGettersAndSetters getPapersGettersAndSetters() {
        return new PapersGettersAndSetters();
    }

    @Bean
    public ConferencesGettersAndSetters getConferencesGettersAndSetters(){return new ConferencesGettersAndSetters();}

    @Bean
    public RemoteFileManager getFileUploader(Environment env) {
        return new DropboxManagerRemote(env.getProperty("dropbox.app.key"),
                env.getProperty("dropbox.app.secret"),
                env.getProperty("dropbox.access.token"));
    }
}
