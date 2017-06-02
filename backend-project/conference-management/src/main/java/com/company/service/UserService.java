package com.company.service;

import com.company.domain.*;
import org.apache.catalina.User;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Optional;

/**
 * Created by AlexandruD on 02-Jun-17.
 */
@Component
public interface UserService {

    boolean login(String username, String password);

    Optional<AppUser> getUser(String username);

    Optional<Privileges> getConferencePrivileges(String username, int confId);

    Iterable<AppUser> getUsers();

    Optional<AppUser> addUser(AppUser u);

    Optional<AppUser> updateUser(AppUser u);

    void updatePrivilegesOfConference(String username, int confId, Privileges newPrivs);

    Optional<Iterable<Paper>> getPapersOfStatus(String username, int paperStatusId);

    Optional<Iterable<Review>> getReviewsOfPaper(String username, int paperId);

    void addBidForPaper(String username, int paperId);

    void uploadPresentation(String username, int paperId, File presentationFile);

    Optional<Iterable<Paper>> getAssignedPapers(String username);

    Optional<Bid> getBidOfPaper(String username, int paperId);

    void assignPaper(String username, int paperId);

    Optional<Iterable<Paper>> getSubmittedPapers(String username);
}
