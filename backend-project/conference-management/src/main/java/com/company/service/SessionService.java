package com.company.service;

import com.company.domain.AppUser;
import com.company.domain.Session;
import com.company.utils.exception.Exceptional;
import com.company.controller.DTOs.SessionDTO;
/**
 * Created by sebi on 6/7/2017.
 */
public interface SessionService {

    /**
     * get the session by id
     * @return the session
     */
    Exceptional<Session> getSession(int sessId);

    /**
     * Gets all sessions from the database
     */
    Iterable<Session> getSessions();

    /**
     *
     * @param s the session
     * @return created session
     */
    Exceptional<Session> addSession(Session s);

    /**
     *
     * @param sessId session id
     * @param sessionDTO new session data
     * @return updated session
     */
    Exceptional<Session> updateSession(int sessId, Session sessionDTO);

    /**
     *
     * @param sessId session id
     * @return deleted session
     */
    //todo: don't know if we really need this, so may be removed
    Exceptional<Session> removeSession(int sessId);

    /**
     *
     * @param sessId
     * @param username new session chair for the session
     * @return updated session
     */
    Exceptional<Session> updateSessionChair(int sessId, String username);

    /**
     *
     * @param sessId
     * @return potential session chairs for a specific session
     */
    Iterable<AppUser> getPotentialChairs(int sessId);

    /**
     *
     * @param sessId a specific session
     * @param userId a listener to the given session
     * @return the updated session
     */
    Exceptional<Session> participateOnSession(int sessId, int userId);
}

