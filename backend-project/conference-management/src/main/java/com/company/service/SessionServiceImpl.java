package com.company.service;

import com.company.domain.AppUser;
import com.company.domain.Session;
import com.company.repository.SessionRepository;
import com.company.utils.exception.Exceptional;

/**
 * Created by sebi on 6/7/2017.
 */
public class SessionServiceImpl implements SessionService {

    private SessionRepository sessionRepository;

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
            return Exceptional.OK(sessionRepository.save(s));
        }catch(Exception e) {
            return Exceptional.Error(e);
        }
    }

    @Override
    public Exceptional<Session> updateSession(int sessId, Session s) {
        Session session = sessionRepository.findOne(sessId);
        if(session == null) {
            return Exceptional.Error(new Exception("Session id not found"));
        }
        this.sessionRepository.delete(sessId);
        this.sessionRepository.save(s);
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
    public Exceptional<Session> updateSessionChair(int sessId, AppUser sessChair) {
        Session session = sessionRepository.findOne(sessId);
        if(session == null) {
            return Exceptional.Error(new Exception("Session id not found"));
        }
        session.setSessionChair(sessChair);
        this.updateSession(sessId, session);
        return Exceptional.OK(session);
    }

    @Override
    //todo not sure how should be implemented   :|
    public Exceptional<Iterable<AppUser>> getPotentialChairs(int sessId) {
        return null;
    }

    @Override
    public Exceptional<Session> participateOnSession(int sessId, AppUser listener) {
        Session session = sessionRepository.findOne(sessId);
        if(session == null) {
            return Exceptional.Error(new Exception("Session id not found"));
        }
        session.getListeners().add(listener);
        return Exceptional.OK(session);
    }
}
