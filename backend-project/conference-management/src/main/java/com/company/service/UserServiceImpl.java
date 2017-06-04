package com.company.service;

import com.company.domain.*;
import com.company.repository.ConferenceRepository;
import com.company.repository.PaperRepository;
import com.company.repository.UserRepository;
import com.company.utils.dropbox.DropboxUploader;
import com.company.utils.updater.PrivilegesGettersAndSetters;
import com.company.utils.updater.Updater;
import com.company.utils.updater.UsersGettersAndSetters;
import com.dropbox.core.DbxException;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.File;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by AlexandruD on 02-Jun-17.
 */
@Component
public class UserServiceImpl implements UserService {

    private UploadedFileService uploadedFileService;
    private ConferenceRepository conferenceRepository;
    private UserRepository userRepository;
    private PaperRepository paperRepository;
    private PasswordEncoder encoder;
    private UsersGettersAndSetters usersGettersAndSetters;
    private PrivilegesGettersAndSetters privilegesGettersAndSetters;
    private Updater updater;
    private Random random;

    public UserServiceImpl(@Autowired UserRepository userRepository,
                           @Autowired PaperRepository paperRepository,
                           @Autowired PasswordEncoder encoder,
                           @Autowired UsersGettersAndSetters usersGettersAndSetters,
                           @Autowired PrivilegesGettersAndSetters privilegesGettersAndSetters,
                           @Autowired ConferenceRepository conferenceRepository,
                           @Autowired UploadedFileService uploadedFileService) {
        this.userRepository = userRepository;
        this.paperRepository = paperRepository;
        this.encoder = encoder;
        this.usersGettersAndSetters = usersGettersAndSetters;
        this.updater = new Updater();
        this.privilegesGettersAndSetters = privilegesGettersAndSetters;
        this.conferenceRepository = conferenceRepository;
        this.uploadedFileService = uploadedFileService;
        random = new Random(System.currentTimeMillis());
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

    @Transactional
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

    @Transactional
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

    @Transactional
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

    @Transactional
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

        Optional<Iterable<Review>> result = Optional.empty();

        if(paperOpt.isPresent()) {
            result = Optional.of(new ArrayList<>(paperOpt.get().getReviews()));
        }

        return result;
    }

    @Transactional
    @Override
    public void addBidForPaper(String username, int paperId, BidStatus status) {
        Paper pap = paperRepository.findOne(paperId);
        if(pap == null)
            return;

        Optional<AppUser> uopt = getUser(username);
        uopt.ifPresent(user -> {
            // If bid for paper already exists, update it
            Optional<Bid> bidopt = user.getBids().stream()
                    .filter(e -> e.getPaper().getId().equals(paperId))
                    .reduce((a, b) -> a);

            if(bidopt.isPresent()) {
                // Update bid status
                bidopt.get().setStatus(status);
            } else {
                // Create new bid
                Bid b = new Bid();
                b.setBidder(user);
                b.setPaper(pap);
                b.setStatus(status);
                user.getBids().add(b);
            }

            userRepository.save(user);
        });
    }

    @Override
    public void uploadPresentation(String username, int paperId, String extension, String presentationFileData) {
        // Save no paper from a non-existant user
        if(!userRepository.userExists(username))
            return;
        // Generate a filename
        String filePath = username + random.nextLong() + extension;
        Paper pap = paperRepository.findOne(paperId);
        if(pap == null) {
            // Return if no paper found
            return;
        }
        Optional<UploadedFile> file = uploadedFileService.uploadFile(filePath, presentationFileData);
        file.ifPresent(uf -> {
            uf.setPaperIsPresentationFor(pap);
            uploadedFileService.saveUploadedFileData(uf);
        });
    }

    @Override
    public Optional<Iterable<Paper>> getAssignedPapers(String username) {
        Iterable<Paper> res = userRepository.getAssignedPapers(username);
        return userRepository.userExists(username)? Optional.of(res) : Optional.empty();
    }

    @Override
    public Optional<Bid> getBidOfPaper(String username, int paperId) {
        Bid b = userRepository.getBidOfPaper(username, paperId);
        return b == null? Optional.empty() : Optional.of(b);
    }

    @Transactional
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

    @Transactional
    @Override
    public void removeAssignedPaper(String username, int paperId) {
        Optional<AppUser> usrOpt = getUser(username);
        usrOpt.ifPresent(e -> {
            Paper p = paperRepository.findOne(paperId);
            if(p != null) {
                e.getAssignedForReview().removeIf(f -> f.getId().equals(paperId));
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
