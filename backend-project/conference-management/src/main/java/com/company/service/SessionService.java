package com.company.service;

import com.company.domain.AppUser;
import com.company.domain.Session;
import com.company.utils.exception.Exceptional;

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
     * @param s new session
     * @return updated session
     */
    Exceptional<Session> updateSession(int sessId, Session s);

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
     * @param sessChair new session chair for the session
     * @return updated session
     */
    Exceptional<Session> updateSessionChair(int sessId, AppUser sessChair);

    /**
     *
     * @param sessId
     * @return potential session chairs for a specific session
     */
    Exceptional<Iterable<AppUser>> getPotentialChairs(int sessId);

    /**
     *
     * @param sessId a specific session
     * @param listener a listener to the given session
     * @return the updated session
     */
    Exceptional<Session> participateOnSession(int sessId, AppUser listener);
}

