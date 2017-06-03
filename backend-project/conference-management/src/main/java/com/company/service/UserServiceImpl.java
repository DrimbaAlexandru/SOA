package com.company.service;

import com.company.domain.*;
import com.company.repository.ConferenceRepository;
import com.company.repository.PaperRepository;
import com.company.repository.UserRepository;
import com.company.utils.updater.PrivilegesGettersAndSetters;
import com.company.utils.updater.Updater;
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

    private ConferenceRepository conferenceRepository;
    private UserRepository userRepository;
    private PaperRepository paperRepository;
    private PasswordEncoder encoder;
    private UsersGettersAndSetters usersGettersAndSetters;
    private PrivilegesGettersAndSetters privilegesGettersAndSetters;
    private Updater updater;

    public UserServiceImpl(@Autowired UserRepository userRepository,
                           @Autowired PaperRepository paperRepository,
                           @Autowired PasswordEncoder encoder,
                           @Autowired UsersGettersAndSetters usersGettersAndSetters,
                           @Autowired PrivilegesGettersAndSetters privilegesGettersAndSetters,
                           @Autowired ConferenceRepository conferenceRepository) {
        this.userRepository = userRepository;
        this.paperRepository = paperRepository;
        this.encoder = encoder;
        this.usersGettersAndSetters = usersGettersAndSetters;
        this.updater = new Updater();
        this.privilegesGettersAndSetters = privilegesGettersAndSetters;
        this.conferenceRepository = conferenceRepository;
    }

    @Override
    public boolean login(String username, String password) {
        AppUser usr = userRepository.findByUsername(username);
        return usr != null && encoder.matches(password, usr.getPassword());
    }

    @Override
    public Optional<AppUser> getUser(String username) {
        AppUser usr = userRepository.findByUsername(username);
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
        return userRepository.findAll();
    }

    @Override
    public Optional<AppUser> addUser(AppUser u) {
        u.setUsername(null);
        // Encrypt password
        u.setPassword(encoder.encode(u.getPassword()));
        return Optional.of(userRepository.save(u));
    }

    @Override
    public Optional<AppUser> updateUser(String username, AppUser u) {

        Optional<AppUser> au = getUser(username);

        au.ifPresent(e -> {
            updater.update(e, u, usersGettersAndSetters.getGettersAndSetters());
            this.userRepository.save(e);
        });
        return au;
    }

    @Override
    public void updatePrivilegesOfConference(String username, int confId, Privileges newPrivs) {
        Optional<AppUser> au = getUser(username);

        au.ifPresent(e -> {
            Optional<Privileges> privs = e.getPrivileges()
                    .stream()
                    .filter(f -> f.getConference().getId().equals(confId))
                    .reduce((a, b) -> a);

            if(privs.isPresent()) {
                Privileges privileges = privs.get();
                updater.update(privileges, newPrivs, privilegesGettersAndSetters.getGettersAndSetters());
            } else {
                Conference conf = conferenceRepository.findOne(confId);
                if(conf == null) {
                    // If the conference does not exist, return
                    return;
                }
                // Create new privileges with the data given if no privileges found
                Privileges privileges = new Privileges(e, conf);
                updater.update(privileges, newPrivs, privilegesGettersAndSetters.getGettersAndSetters());
                e.getPrivileges().add(privileges);
            }
            userRepository.save(e);
        });
    }

    @Override
    public Optional<Iterable<Paper>> getPapersOfStatus(String username, PaperStatus status) {
        Optional<AppUser> opt = getUser(username);

        return opt.isPresent()?
            Optional.of(
                opt.get().getSubmittedPapers()
                    .stream()
                    .filter(e -> e.getStatus().equals(status))
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
        Iterable<Paper> res = userRepository.getAssignedPapers(username);
        return !userRepository.userExists(username)? Optional.of(res) : Optional.empty();
    }

    @Override
    public Optional<Bid> getBidOfPaper(String username, int paperId) {
        Bid b = userRepository.getBidOfPaper(username, paperId);
        return b == null? Optional.empty() : Optional.of(b);
    }

    @Override
    public void assignPaper(String username, int paperId) {
        Optional<AppUser> usrOpt = getUser(username);
        usrOpt.ifPresent(e -> {
            Paper p = paperRepository.findOne(paperId);
            if(p != null) {
                e.getAssignedForReview().add(p);
                userRepository.save(e);
            }
        });
    }

    @Override
    public Optional<Iterable<Paper>> getSubmittedPapers(String username) {
        Iterable<Paper> res = userRepository.getSubmittedPapers(username);
        return !userRepository.userExists(username)? Optional.empty() : Optional.of(res);
    }
}
