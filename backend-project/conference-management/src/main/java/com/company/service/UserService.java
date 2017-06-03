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

    /**
     * Checks if the provided username and password match a user in the database
     * @param username The username
     * @param password The password
     */
    boolean login(String username, String password);

    /**
     * Gets a user by username
     * @param username The username
     * @return Optional.empty() if the user is not found, an Optional with the user otherwise
     */
    Optional<AppUser> getUser(String username);

    /**
     * Gets the conference-level privileges of a given user
     * @param username The username
     * @param confId The conference id
     * @return Optional.empty() if the user or conference does not exist, an Optional with the data otherwise
     */
    Optional<Privileges> getConferencePrivileges(String username, int confId);

    /**
     * Gets all users from the database
     */
    Iterable<AppUser> getUsers();

    /**
     * Adds a user to the database
     * @param u The user
     * @return The user, Optional.empty() if the add fails
     */
    Optional<AppUser> addUser(AppUser u);

    /**
     * Updates a user
     * @param username The username
     * @param u The user data
     * @return Optional.empty() if the user does not exist, the modified user otherwise
     */
    Optional<AppUser> updateUser(String username, AppUser u);

    /**
     * Updates the privileges of a conference. If the user has no privileges for a given conference, the privileges are added.
     * @param username The username
     * @param confId The conference id
     * @param newPrivs The new privileges
     */
    void updatePrivilegesOfConference(String username, int confId, Privileges newPrivs);

    /**
     * Returns all papers having a given status
     * @param username The username
     * @param status The paper status
     * @return Optional.empty() if the user does not exist, Optional with the data otherwise
     */
    Optional<Iterable<Paper>> getPapersOfStatus(String username, PaperStatus status);

    /**
     * Gets all reviews of a paper submitted by the given user
     * @param username The username
     * @param paperId The paper
     * @return Optional.empty() if the user or paper do not exist, an Optional with the data otherwise
     */
    Optional<Iterable<Review>> getReviewsOfPaper(String username, int paperId);

    /**
     * Adds the bid of a user to the paper
     * @param username The username
     * @param paperId The paper
     */
    void addBidForPaper(String username, int paperId);

    /**
     * Uploads a user's presentation of a paper
     * @param username The username
     * @param paperId The paper id
     * @param presentationFile The presentation file
     */
    void uploadPresentation(String username, int paperId, File presentationFile);

    /**
     * Gets all papers assigned for review to a given user
     * @param username The username
     * @return Optional.empty() if the user does not exist, an Optional with the data otherwise
     */
    Optional<Iterable<Paper>> getAssignedPapers(String username);

    /**
     * Gets a users bid of a paper
     * @param username The username
     * @param paperId The paper id
     * @return Optional.empty() if the user or paper do not exist, an Optional with the data otherwise
     */
    Optional<Bid> getBidOfPaper(String username, int paperId);

    /**
     * Assigns a paper to a user for reviewing purposes
     * @param username The username
     * @param paperId The paper id
     */
    void assignPaper(String username, int paperId);

    /**
     * Gets all papers submitted by a user
     * @param username The username
     * @return Optional.empty() if the user does not exist, an Optional with the data otherwise
     */
    Optional<Iterable<Paper>> getSubmittedPapers(String username);
}
