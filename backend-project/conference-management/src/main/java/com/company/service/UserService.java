package com.company.service;

import com.company.domain.*;
import com.company.utils.exception.Exceptional;

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
     * Returns all subjects of interest for a given user
     * @param username The username
     */
    Exceptional<Iterable<SubjectOfInterest>> getSubjectsOfInterest(String username);

    /**
     * Adds a new subject of interest for a given user
     * @param username The username
     * @param searchString The searchString
     */
    Exceptional<SubjectOfInterest> addSubjectOfInterest(String username, String searchString );

    /**
     * Updates an existing subject of interest for a given user
     * @param username The username
     * @param SoIId The id of the SubjectOfInterest
     */
    Exceptional<SubjectOfInterest> updateSubjectOfInterest(String username, Integer SoIId);

}
