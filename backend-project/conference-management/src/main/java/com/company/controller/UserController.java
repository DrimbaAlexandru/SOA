package com.company.controller;

import com.company.domain.AppUser;
import com.company.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Created by AlexandruD on 02-Jun-17.
 */
@RestController
@RequestMapping(path = "/users")
public class UserController {

    private UserService service;

    public UserController(@Autowired UserService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Iterable<AppUser> getUsers() {
        return service.getUsers();
    }

    @RequestMapping(path="/{username}", method = RequestMethod.POST)
    public void updateUser(@PathVariable("username") String username,
                           AppUser user) {

        Optional<AppUser> res = service.updateUser(username, user);
    }
}
