package com.company;

import com.company.utils.FileUploader;
import com.company.utils.dropbox.DropboxUploader;
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
    public FileUploader getFileUploader(Environment env) {
        return new DropboxUploader(env.getProperty("dropbox.app.key"), env.getProperty("dropbox.app.secret"));
    }
}
