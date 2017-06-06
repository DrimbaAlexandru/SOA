package com.company.service;

import com.company.domain.Conference;
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

    Exceptional<Conference> addConference(Conference c);



}
