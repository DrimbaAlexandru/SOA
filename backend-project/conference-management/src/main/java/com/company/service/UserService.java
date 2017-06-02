package com.company.service;

import com.company.domain.AppUser;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by AlexandruD on 02-Jun-17.
 */
@Component
public interface UserService {

    boolean login(String username, String password);

    Optional<AppUser> getUser(String username);


}
