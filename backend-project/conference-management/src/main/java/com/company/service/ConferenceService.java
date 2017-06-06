package com.company.service;

import com.company.domain.Conference;
import com.company.domain.Session;
import com.company.utils.exception.Exceptional;

/**
 * Created by sebi on 6/6/2017.
 */
public interface ConferenceService {
    /**
     * get the conference by id
     * @return the conference
     */
    Exceptional<Conference> getConference(int confId);

    /**
     * Gets all conferences from the database
     */
    Iterable<Conference> getConferences();

    /**
     *
     * @param c the conference
     * @return created conference
     */
    Exceptional<Conference> addConference(Conference c);

    /**
     *
     * @param id conference id
     * @param c new conference
     * @return updated conference
     */
    Exceptional<Conference> updateConference(int id, Conference c);

    /**
     *
     * @param id conference id
     * @return deleted conference
     */
    Exceptional<Conference> removeConference(int id);

    /**
     *
     * @param confId conference Id
     * @return all sessions of the conference
     */
    Iterable<Session> getSessions(int confId);

}
