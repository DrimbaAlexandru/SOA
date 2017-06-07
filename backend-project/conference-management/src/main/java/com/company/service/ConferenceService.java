package com.company.service;

import com.company.controller.DTOs.ConferenceDTO;
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

    Exceptional<Conference> addConference(ConferenceDTO c);

    /**
     *
     * @param confId conference id
     * @param c new conference
     * @return updated conference
     */
    Exceptional<Conference> updateConference(int confId, Conference c);

    Exceptional<Conference> updateConference(int oldId, ConferenceDTO c);

    /**
     *
     * @param confId conference id
     * @return deleted conference
     */
    Exceptional<Conference> removeConference(int confId);

    /**
     *
     * @param confId conference Id
     * @return all sessions of the conference
     */
    Exceptional<Iterable<Session>> getSessions(int confId);

}
