package com.company.service;

import com.company.controller.DTOs.ConferenceDTO;
import com.company.domain.Conference;
import com.company.domain.Session;
import com.company.repository.ConferenceRepository;
import com.company.utils.exception.Exceptional;
import com.company.utils.updater.ConferencesGettersAndSetters;
import com.company.utils.updater.Updater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;

/**
 * Created by sebi on 6/6/2017.
 */
@Component
public class ConferenceServiceImpl implements ConferenceService {

    private ConferenceRepository conferenceRepository;
    private Updater updater;
    private ConferencesGettersAndSetters conferencesGettersAndSetters;

    public ConferenceServiceImpl(
            @Autowired ConferenceRepository conferenceRepository,
            @Autowired ConferencesGettersAndSetters conferencesGettersAndSetters) {
        this.conferenceRepository = conferenceRepository;
        this.updater = new Updater();
        this.conferencesGettersAndSetters = conferencesGettersAndSetters;
    }

    @Override
    public Exceptional<Conference> getConference(int confId) {
        Conference conf = conferenceRepository.findOne(confId);

        return conf == null?
                Exceptional.Error(new Exception("Conference id not found")) :
                Exceptional.OK(conf);
    }

    @Override
    public Iterable<Conference> getConferences() {
        return conferenceRepository.findAll();
    }

    @Override
    public Exceptional<Conference> addConference(Conference c) {
        try {
            return Exceptional.OK(conferenceRepository.save(c));
        }catch(Exception e) {
            return Exceptional.Error(e);
        }
    }

    @Override
    public Exceptional<Conference> addConference(ConferenceDTO c) {
        Conference conference =new Conference(c.getName(),c.getEventTimeSpan().getStartDate(), c.getEventTimeSpan().getEndDate(), c.getCallForAbstractTimeSpan().getEndDate(), c.getCallForProposalsTimeSpan().getEndDate(), c.getBiddingDeadline());
        return Exceptional.OK(conferenceRepository.save(conference));
    }

    @Override
    public Exceptional<Conference> updateConference(int confId, Conference c) {
        Conference conference = conferenceRepository.findOne(confId);
        if(conference == null) {
            return Exceptional.Error(new Exception("Conference id not found"));
        }
        updater.update(conference, c, conferencesGettersAndSetters.getGettersAndSetters());
        this.conferenceRepository.save(c);
        return Exceptional.OK(conference);
    }

    @Override
    public Exceptional<Conference> removeConference(int confId) {

        Conference conference = conferenceRepository.findOne(confId);
        if(conference == null) {
            return Exceptional.Error(new Exception("Conference id not found"));
        }
        conferenceRepository.delete(confId);
        return Exceptional.OK(conference);
    }

    @Override
    public Exceptional<Iterable<Session>> getSessions(int confId) {
        Iterable<Session> res = conferenceRepository.getSessions(confId);
        return (conferenceRepository.findOne(confId) == null)?
                Exceptional.Error(new Exception("Conference id not found")) :
                Exceptional.OK(res);


/*
        Conference au = conferenceRepository.findOne(confId);

        if(au == null) {
            return Exceptional.Error(new Exception("Conference id not found"));
        }

        Optional<Session> sessionOptional = au.getSessions()
                .stream()
                .filter(e -> e.getId() == confId)
                .reduce((a ,b) -> a);

        if(sessionOptional.isPresent()) {
            return Exceptional.OK(new ArrayList<Session>(sessionOptional.);
        } else {
            return Exceptional.Error(new Exception("No sessions found for the given conference id"));
        }*/
    }
}
