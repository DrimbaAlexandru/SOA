package com.company.controller;

import com.company.domain.AppUser;
import com.company.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(path = "/{username}/submittedPapers/{paperId}/presentation",
            method = RequestMethod.POST)
    public void uploadPresentation(@PathVariable("username") String username,
                                   @PathVariable("paperId") Integer paperId,
                                   @RequestBody PresentationPostDTO data) {

        service.uploadPresentation(username,
                paperId,
                "." + data.getType(),
                data.getData());
    }


    private class PresentationPostDTO {
        private String type;
        private byte[] data;

        public PresentationPostDTO() {

        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public byte[] getData() {
            return data;
        }

        public void setData(byte[] data) {
            this.data = data;
        }
    }
}
