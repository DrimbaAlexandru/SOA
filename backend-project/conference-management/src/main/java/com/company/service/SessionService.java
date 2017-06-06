package com.company.service;

import com.company.domain.Session;
import com.company.utils.exception.Exceptional;

/**
 * Created by sebi on 6/7/2017.
 */
public interface SessionService {

    /**
     * get the conference by id
     * @return the conference
     */
    Exceptional<Session> getSession(int confId);

    /**
     * Gets all conferences from the database
     */
    Iterable<Session> getSessions();

    /**
     *
     * @param s the conference
     * @return created conference
     */
    Exceptional<Session> addSession(Session s);

    /**
     *
     * @param confId conference id
     * @param s new conference
     * @return updated conference
     */
    Exceptional<Session> updateSession(int confId, Session s);

    /**
     *
     * @param confId conference id
     * @return deleted conference
     */
    Exceptional<Session> removeSession(int confId);
}

