package com.company.service;

import com.company.domain.AppUser;
import com.company.domain.Conference;
import com.company.domain.Session;
import com.company.repository.ConferenceRepository;
import com.company.repository.SessionRepository;
import com.company.utils.container.Container;
import com.company.utils.exception.Exceptional;
import com.company.controller.DTOs.SessionDTO;
import com.company.utils.updater.SessionGettersAndSetters;
import com.company.utils.updater.Updater;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;

/**
 * Created by sebi on 6/7/2017.
 */
public class SessionServiceImpl implements SessionService {

    private UserService userService;
    private SessionRepository sessionRepository;
    private ConferenceRepository conferenceRepository;
    private Updater updater;
    private SessionGettersAndSetters sessionsGettersAndSetters;

    public SessionServiceImpl(@Autowired SessionRepository sessionRepository,
                              @Autowired ConferenceRepository conferenceRepository,
                              @Autowired SessionGettersAndSetters sessionGettersAndSetters,
                              @Autowired UserService userService) {

        this.sessionRepository = sessionRepository;
        this.conferenceRepository = conferenceRepository;
        this.sessionsGettersAndSetters = sessionGettersAndSetters;
        this.userService = userService;
        updater = new Updater();
    }

    @Override
    public Exceptional<Session> getSession(int sessId) {
        Session session = sessionRepository.findOne(sessId);

        return session == null?
                Exceptional.Error(new Exception("Session id not found")) :
                Exceptional.OK(session);
    }

    @Override
    public Iterable<Session> getSessions() {
        return sessionRepository.findAll();
    }

    @Override
    public Exceptional<Session> addSession(Session s) {
        try {

            Conference c = conferenceRepository.findOne(s.getId());
            if(c == null) {
                return Exceptional.Error(new Exception("Conference not found"));
            }

            s.setConference(c);

            return Exceptional.OK(sessionRepository.save(s));
        }catch(Exception e) {
            return Exceptional.Error(e);
        }
    }

    @Override
    public Exceptional<Session> updateSession(int sessId, Session sess) {
        Session session = sessionRepository.findOne(sessId);
        if(session == null) {
            return Exceptional.Error(new Exception("Session id not found"));
        }
        Conference conf = conferenceRepository.findOne(sess.getConference().getId());

        if(conf == null) {
            return Exceptional.Error(new Exception("Conference not found"));
        }

        updater.update(session, sess, sessionsGettersAndSetters.getGettersAndSetters());

        this.sessionRepository.save(session);
        return Exceptional.OK(session);
    }

    @Override
    public Exceptional<Session> removeSession(int sessId) {
        Session session = sessionRepository.findOne(sessId);
        if(session == null) {
            return Exceptional.Error(new Exception("Session id not found"));
        }
        this.sessionRepository.delete(sessId);
        return Exceptional.OK(session);
    }

    @Override
    public Exceptional<Session> updateSessionChair(int sessId, String username) {

        Session session = sessionRepository.findOne(sessId);
        if(session == null) {
            return Exceptional.Error(new Exception("Session id not found"));
        }

        Exceptional<AppUser> au = userService.getUser(username);

        StringBuilder errors = new StringBuilder();

        au.error(e -> {
            errors.append(e.getMessage());
        }).ok(e -> {
            session.setSessionChair(e);
            sessionRepository.save(session);
        });

        if(errors.length() != 0) {
            return Exceptional.Error(new Exception(errors.toString()));
        }

        return Exceptional.OK(session);
    }

    @Override
    //todo not sure how should be implemented   :|
    public Iterable<AppUser> getPotentialChairs(int sessId) {
        return null;
    }

    @Transactional
    @Override
    public Exceptional<Session> participateOnSession(int sessId, int userId) {
        Session session = sessionRepository.findOne(sessId);

        if(session == null) {
            return Exceptional.Error(new Exception("Session id not found"));
        }

        Exceptional<AppUser> user = userService.getUserById(userId);
        StringBuilder err = new StringBuilder();

        Container<AppUser> cont = new Container<>(null);
        user.error(e -> {
           err.append(e.getMessage());
        }).ok(e -> {
            cont.setValue(e);
        });

        if(err.length() != 0) {
            return Exceptional.Error(new Exception(err.toString()));
        }

        session.getListeners().add(cont.getValue());
        sessionRepository.save(session);
        return Exceptional.OK(session);
    }
    
}
