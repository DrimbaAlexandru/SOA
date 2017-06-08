package com.company.service;

import com.company.domain.*;
import com.company.repository.ConferenceRepository;
import com.company.repository.PaperRepository;
import com.company.repository.UserRepository;
import com.company.utils.container.Container;
import com.company.utils.exception.Exceptional;
import com.company.utils.updater.PrivilegesGettersAndSetters;
import com.company.utils.updater.Updater;
import com.company.utils.updater.UsersGettersAndSetters;
import com.sun.javaws.exceptions.InvalidArgumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

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
    public Exceptional<AppUser> getUser(String username) {
        AppUser usr = userRepository.findByUsername(username);

        return usr == null?
                Exceptional.Error(new Exception("Username not found")) :
                Exceptional.OK(usr);
    }

    @Transactional
    @Override
    public Exceptional<Privileges> getConferencePrivileges(String username, int confId) {

        Conference c=conferenceRepository.findOne(confId);

        if(c==null)
            return Exceptional.Error(new Exception("There's no conference with the given ID"));

        AppUser au = userRepository.findByUsername(username);

        if(au == null) {
            return Exceptional.Error(new Exception("User not found"));
        }

        Optional<Privileges> privs = au.getPrivileges()
                .stream()
                .filter(e -> e.getConference().getId() == confId)
                .reduce((a, b) -> a);

        Privileges finalPrivs;

        if(!privs.isPresent()) {
            finalPrivs = new Privileges(au, conferenceRepository.findOne(confId));
        } else {
            finalPrivs = privs.get();
        }

        return Exceptional.OK(finalPrivs);
    }

    @Override
    public Iterable<AppUser> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public Exceptional<AppUser> addUser(AppUser u) {
        // Encrypt password
        u.setPassword(encoder.encode(u.getPassword()));
        try {
            return Exceptional.OK(userRepository.save(u));
        }catch(Exception e) {
            return Exceptional.Error(e);
        }
    }

    @Override
    public Exceptional<AppUser> updateUser(String username, AppUser u) {

        AppUser au = userRepository.findByUsername(username);
        if(u.getPassword()!=null)
            u.setPassword(encoder.encode(u.getPassword()));
        if(au == null) {
            return Exceptional.Error(new Exception("Username not found"));
        }
        updater.update(au, u, usersGettersAndSetters.getGettersAndSetters());
        this.userRepository.save(au);
        return Exceptional.OK(au);
    }

    @Transactional
    @Override
    public Exceptional<Void> updatePrivilegesOfConference(String username, int confId, Privileges newPrivs) {
        AppUser e = userRepository.findByUsername(username);

        if(e == null) {
            return Exceptional.Error(new Exception("Username not found"));
        }

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
                return Exceptional.Error(new Exception("Conference not found"));
            }
            // Create new privileges with the data given if no privileges found
            Privileges privileges = new Privileges(e, conf);
            updater.update(privileges, newPrivs, privilegesGettersAndSetters.getGettersAndSetters());
            e.getPrivileges().add(privileges);
        }
        userRepository.save(e);

        return Exceptional.OK(null);
    }

    @Transactional
    @Override
    public Exceptional<Iterable<Paper>> getPapersOfStatus(String username, PaperStatus status) {
        AppUser au = userRepository.findByUsername(username);

        return au != null?
                Exceptional.OK(
                        au.getSubmittedPapers()
                                .stream()
                                .filter(e -> e.getStatus().equals(status))
                                .collect(Collectors.toList())
                ) : Exceptional.Error(new Exception("User not found"));
    }

    @Transactional
    @Override
    public Exceptional<Iterable<Review>> getReviewsOfPaper(String username, int paperId) {
        AppUser au = userRepository.findByUsername(username);

        if(au == null) {
            return Exceptional.Error(new Exception("Username not found"));
        }

        Optional<Paper> paperOpt = au.getSubmittedPapers()
                .stream()
                .filter(e -> e.getId() == paperId)
                .reduce((a ,b) -> a);

        if(paperOpt.isPresent()) {
            return Exceptional.OK(new ArrayList<>(paperOpt.get().getReviews()));
        } else {
            return Exceptional.Error(new Exception("No papers found for the given user"));
        }
    }

    @Transactional
    @Override
    public Exceptional<Review> getReviewOfPaper(String username, int paperId) {
        AppUser au = userRepository.findByUsername(username);

        if(au == null)
            return Exceptional.Error(new Exception("Username not found"));

        if(paperRepository.findOne(paperId)==null)
            return Exceptional.Error(new Exception("Paper Id is not valid"));

        Optional<Review> reviewOpt = au.getReviews().stream().filter(r->{return r.getPaper().getId()==paperId;}).findFirst();

        if(reviewOpt.isPresent()) {
            return Exceptional.OK(reviewOpt.get());
        } else {
            return Exceptional.OK(null);
        }
    }

    @Transactional
    @Override
    public Exceptional<Void> addBidForPaper(String username, int paperId, BidStatus status) {
        Paper pap = paperRepository.findOne(paperId);
        if(pap == null)
            return Exceptional.Error(new Exception("No paper found"));

        AppUser user = userRepository.findByUsername(username);

        if(user == null) {
            return Exceptional.Error(new Exception("User not found"));
        }

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

        return Exceptional.OK(null);
    }

    @Override
    public Exceptional<Void> uploadPresentation(String username, int paperId, String extension, byte[] presentationFileData) {
        // Save no paper from a non-existant user
        if(!userRepository.userExists(username))
            return Exceptional.Error(new Exception("User not found"));
        // Generate a filename
        String filePath = "/" + username + "--" + random.nextLong() + extension;
        Paper pap = paperRepository.findOne(paperId);
        if(pap == null) {
            // Return if no paper found
            return Exceptional.Error(new Exception("No paper found"));
        }
        Exceptional<UploadedFile> file = uploadedFileService.uploadFile(filePath, presentationFileData);
        Container<UploadedFile> cont = new Container<>(null);
        StringBuilder errors = new StringBuilder();
        file.error(e -> {
            errors.append(e.getMessage());
        }).ok(e -> {
            cont.setValue(e);
        });

        if(errors.length() != 0) {
            return Exceptional.Error(new Exception(errors.toString()));
        } else {
            cont.getValue().setPaperIsPresentationFor(pap);
            uploadedFileService.saveUploadedFileData(cont.getValue());
            return Exceptional.OK(null);
        }
    }

    @Override
    public Exceptional<Iterable<Paper>> getAssignedPapers(String username) {
        Iterable<Paper> res = userRepository.getAssignedPapers(username);
        return !userRepository.userExists(username)?
                Exceptional.Error(new Exception("Username not found")) :
                Exceptional.OK(res);
    }

    @Override
    public Exceptional<Bid> getBidOfPaper(String username, int paperId) {
        if(userRepository.findByUsername(username)==null)
            return Exceptional.Error(new Exception("User not found"));
        if(paperRepository.findOne(paperId)==null)
            return Exceptional.Error(new Exception("Paper not found"));
        Bid b = userRepository.getBidOfPaper(username, paperId);
        return b == null? Exceptional.OK(null) :
                Exceptional.OK(b);
    }

    @Transactional
    @Override
    public Exceptional<Void> assignPaper(String username, int paperId) {
        AppUser user = userRepository.findByUsername(username);
        if(user == null) {
            return Exceptional.Error(new Exception("Username not found"));
        }

        Paper p = paperRepository.findOne(paperId);
        if(p == null) {
            return Exceptional.Error(new Exception("Paper not found"));
        }

        user.getAssignedForReview().add(p);
        userRepository.save(user);
        return Exceptional.OK(null);
    }

    @Transactional
    @Override
    public Exceptional<Void> removeAssignedPaper(String username, int paperId) {
        AppUser user = userRepository.findByUsername(username);
        if(user == null) {
            return Exceptional.Error(new Exception("Username not found"));
        }
        Paper p = paperRepository.findOne(paperId);
        if(p == null) {
            return Exceptional.Error(new Exception("Paper not found"));
        }
        user.getAssignedForReview().removeIf(f -> f.getId().equals(paperId));
        userRepository.save(user);
        return Exceptional.OK(null);
    }

    @Override
    public Exceptional<Iterable<Paper>> getSubmittedPapers(String username) {
        Iterable<Paper> res = userRepository.getSubmittedPapers(username);
        return !userRepository.userExists(username)? Exceptional.Error(new Exception("User not found")) :
                Exceptional.OK(res);
    }

    @Transactional
    @Override
    public Exceptional<Void> addReviewToPaper(String username, int paperId, ReviewStatus status, String justification) {
        AppUser user = userRepository.findByUsername(username);

        if(user == null) {
            return Exceptional.Error(new Exception("User not found"));
        }

        Paper p = paperRepository.findOne(paperId);

        if(p == null)
            // If the paper does not exist, abort
            return Exceptional.Error(new Exception("Paper does not exist"));

        if(user.getSubmittedPapers()
                .stream()
                .filter(f -> f.getId().equals(paperId))
                .count() >= 1) {

            // If the paper is submitted by this user, abort
            return Exceptional.Error(new Exception("Paper cannot be reviewed by its author"));
        }

        Review rev = new Review();
        getReviewOfPaper(username,paperId).ok(r->
        {
            if(r==null){
                rev.setPaper(p);
                rev.setReviewer(user);
                rev.setStatus(status);
                rev.setJustification(justification);
                user.getReviews().add(rev);
                userRepository.save(user);
            }
            else
            {
                r.setJustification(justification);
                r.setStatus(status);
            }
        });
        return Exceptional.OK(null);
    }
}