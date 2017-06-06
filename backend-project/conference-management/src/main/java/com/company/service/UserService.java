package com.company.service;

import com.company.domain.*;
import com.company.utils.exception.Exceptional;

/**
 * Created by AlexandruD on 02-Jun-17.
 */
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
     */
    Exceptional<AppUser> getUser(String username);

    /**
     * Gets the conference-level privileges of a given user
     * @param username The username
     * @param confId The conference id
     */
    Exceptional<Privileges> getConferencePrivileges(String username, int confId);

    /**
     * Gets all users from the database
     */
    Iterable<AppUser> getUsers();

    /**
     * Adds a user to the database
     * @param u The user
     */
    Exceptional<AppUser> addUser(AppUser u);

    /**
     * Updates a user
     * @param username The username
     * @param u The user data
     */
    Exceptional<AppUser> updateUser(String username, AppUser u);

    /**
     * Updates the privileges of a conference. If the user has no privileges for a given conference, the privileges are added.
     * @param username The username
     * @param confId The conference id
     * @param newPrivs The new privileges
     */
    Exceptional<Void> updatePrivilegesOfConference(String username, int confId, Privileges newPrivs);

    /**
     * Returns all papers having a given status
     * @param username The username
     * @param status The paper status
     */
    Exceptional<Iterable<Paper>> getPapersOfStatus(String username, PaperStatus status);

    /**
     * Gets all reviews of a paper submitted by the given user
     * @param username The username
     * @param paperId The paper
     */
    Exceptional<Iterable<Review>> getReviewsOfPaper(String username, int paperId);

    /**
     * Adds the bid of a user to the paper
     * @param username The username
     * @param paperId The paper
     * @param status The bid status
     */
    Exceptional<Void> addBidForPaper(String username, int paperId, BidStatus status);

    /**
     * Uploads a user's presentation of a paper
     * @param username The username
     * @param paperId The paper id
     * @param extension The paper file extension
     * @param presentationFileData The presentation file data
     */
    Exceptional<Void> uploadPresentation(String username, int paperId, String extension, byte[] presentationFileData);

    /**
     * Gets all papers assigned for review to a given user
     * @param username The username
     */
    Exceptional<Iterable<Paper>> getAssignedPapers(String username);

    /**
     * Gets a users bid of a paper
     * @param username The username
     * @param paperId The paper id
     */
    Exceptional<Bid> getBidOfPaper(String username, int paperId);

    /**
     * Assigns a paper to a user for reviewing purposes
     * @param username The username
     * @param paperId The paper id
     */
    Exceptional<Void> assignPaper(String username, int paperId);

    /**
     * Gets all papers submitted by a user
     * @param username The username
     */
    Exceptional<Iterable<Paper>> getSubmittedPapers(String username);

    /**
     * Removes a paper review assignment from a user
     * @param username The username
     * @param paperId The paper id
     */
    Exceptional<Void> removeAssignedPaper(String username, int paperId);

    /**
     * Adds the review of a user to the paper
     * @param username The username
     * @param paperId The paper
     * @param status The review status
     * @param justification The review justification
     */
    Exceptional<Void> addReviewToPaper(String username, int paperId, ReviewStatus status, String justification);

}
