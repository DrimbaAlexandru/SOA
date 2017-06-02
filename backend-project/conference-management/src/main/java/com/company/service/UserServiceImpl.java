package com.company.service;

import com.company.domain.*;
import com.company.repository.PaperRepository;
import com.company.repository.UserRepository;
import com.company.utils.updater.UsersGettersAndSetters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by AlexandruD on 02-Jun-17.
 */
@Component
public class UserServiceImpl implements UserService {

    private UserRepository userRepostiroy;
    private PaperRepository paperRepository;
    private PasswordEncoder encoder;
    private UsersGettersAndSetters usersGettersAndSetters;

    public UserServiceImpl(@Autowired UserRepository userRepostiroy,
                           @Autowired PaperRepository paperRepository,
                           @Autowired PasswordEncoder encoder,
                           @Autowired UsersGettersAndSetters usersGettersAndSetters) {
        this.userRepostiroy = userRepostiroy;
        this.paperRepository = paperRepository;
        this.encoder = encoder;
        this.usersGettersAndSetters = usersGettersAndSetters;
    }

    @Override
    public boolean login(String username, String password) {
        AppUser usr = userRepostiroy.findByUsername(username);
        return usr != null && encoder.matches(password, usr.getPassword());
    }

    @Override
    public Optional<AppUser> getUser(String username) {
        AppUser usr = userRepostiroy.findByUsername(username);
        return usr == null? Optional.empty() : Optional.of(usr);
    }

    @Override
    public Optional<Privileges> getConferencePrivileges(String username, int confId) {
        Optional<AppUser> opt = getUser(username);

        return opt.isPresent()? opt.get().getPrivileges()
                .stream()
                .filter(e -> e.getId() == confId)
                .reduce((a, b) -> a) : Optional.empty();
    }

    @Override
    public Iterable<AppUser> getUsers() {
        return userRepostiroy.findAll();
    }

    @Override
    public Optional<AppUser> addUser(AppUser u) {
        u.setUsername(null);
        // Encrypt password
        u.setPassword(encoder.encode(u.getPassword()));
        return Optional.of(userRepostiroy.save(u));
    }

    @Override
    public Optional<AppUser> updateUser(AppUser u) {
        return null;
    }

    @Override
    public void updatePrivilegesOfConference(String username, int confId, Privileges newPrivs) {

    }

    @Override
    public Optional<Iterable<Paper>> getPapersOfStatus(String username, int paperStatusId) {
        Optional<AppUser> opt = getUser(username);

        return opt.isPresent()?
            Optional.of(
                opt.get().getSubmittedPapers()
                    .stream()
                    .filter(e -> e.getStatus().getId() == paperStatusId)
                    .collect(Collectors.toList())
            ) : Optional.empty();
    }

    @Override
    public Optional<Iterable<Review>> getReviewsOfPaper(String username, int paperId) {
        Optional<AppUser> opt = getUser(username);

        if(!opt.isPresent()) {
            return Optional.empty();
        }

        Optional<Paper> paperOpt = opt.get().getSubmittedPapers()
                .stream()
                .filter(e -> e.getId() == paperId)
                .reduce((a ,b) -> a);

        return paperOpt.isPresent()? Optional.of(paperOpt.get().getReviews()) : Optional.empty();
    }

    @Override
    public void addBidForPaper(String username, int paperId) {

    }

    @Override
    public void uploadPresentation(String username, int paperId, File presentationFile) {

    }

    @Override
    public Optional<Iterable<Paper>> getAssignedPapers(String username) {
        Iterable<Paper> res = userRepostiroy.getAssignedPapers(username);
        return res != null? Optional.of(res) : Optional.empty();
    }

    @Override
    public Optional<Bid> getBidOfPaper(String username, int paperId) {
        Bid b = userRepostiroy.getBidOfPaper(username, paperId);
        return b == null? Optional.empty() : Optional.of(b);
    }

    @Override
    public void assignPaper(String username, int paperId) {
        Optional<AppUser> usrOpt = getUser(username);
        usrOpt.ifPresent(e -> {
            Paper p = paperRepository.findOne(paperId);
            if(p != null) {
                e.getAssignedForReview().add(p);
                userRepostiroy.save(e);
            }
        });
    }

    @Override
    public Optional<Iterable<Paper>> getSubmittedPapers(String username) {
        Iterable<Paper> res = userRepostiroy.getSubmittedPapers(username);
        return res == null? Optional.empty() : Optional.of(res);
    }
}
