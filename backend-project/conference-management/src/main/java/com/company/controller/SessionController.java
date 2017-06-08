package com.company.controller;

import com.company.controller.DTOs.SessionDTO;

import com.company.controller.DTOs.UserIdRequest;
import com.company.domain.AppUser;
import com.company.domain.Session;
import com.company.domain.SessionSchedule;
import com.company.controller.DTOs.UserResponse;
import com.company.service.SessionService;
import com.company.controller.DTOs.SessionDTO;
import com.company.utils.ResponseJSON;
import com.company.utils.exception.Exceptional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;
/**
 *
 * Created by Utilizator on 6/8/2017.
 */

@RestController
@RequestMapping("/sessions")
public class SessionController {

    private SessionService sessionService;

    public SessionController(@Autowired SessionService sessionService)
    {
        this.sessionService = sessionService;
    }

    @RequestMapping(path="/",method = RequestMethod.GET)
    public ResponseEntity<ResponseJSON<Iterable<SessionDTO>>> handle_get_all()
    {
        List<SessionDTO> sessions = new ArrayList<>();
        for(Session s:sessionService.getSessions())
            sessions.add(new SessionDTO(s));
        ResponseJSON<Iterable<SessionDTO>> resp = new ResponseJSON<>(sessions);
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }


    @RequestMapping(path = "/{sessionId}", method = RequestMethod.GET)
    public ResponseEntity<ResponseJSON<SessionDTO>> handle_get_session(@PathVariable("sessionId") Integer sessionId )
    {
        Exceptional<Session> sessionExceptional = sessionService.getSession(sessionId);
        ResponseJSON<SessionDTO> resp = new ResponseJSON<>();
        sessionExceptional.error(e->{resp.addError(e.getMessage());}).ok(file->{resp.setResp(new SessionDTO(file));});

        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @RequestMapping(path="/", method = RequestMethod.POST)
    public ResponseEntity<ResponseJSON<String>> handle_post_session(
            @RequestBody Session  session)
    {
        ResponseJSON<String> resp = new ResponseJSON<>();
        sessionService.addSession(session).error(e->{resp.setResp(e.getMessage());});
        return new ResponseEntity<>(resp,HttpStatus.OK);
    }

    @RequestMapping(path = "/{sessionId}", method =  RequestMethod.PUT)
    public ResponseEntity<ResponseJSON<String>> handle_put_session(
            @RequestBody Session session,
            @PathVariable("sessionId") int sessionId
    ){
        ResponseJSON<String> resp = new ResponseJSON<>();
        sessionService.updateSession(sessionId,session).error(e->{resp.setResp(e.getMessage());});
        return new ResponseEntity<>(resp,HttpStatus.OK);
    }

    @RequestMapping(path="/{sessionId}",method =  RequestMethod.GET)
    public ResponseEntity<ResponseJSON<Iterable<UserResponse>>> handle_getpotentialChairs(@PathVariable("sessionId") int sessionId)
    {
        List<UserResponse> users= new ArrayList<>();
        for(AppUser u : sessionService.getPotentialChairs(sessionId))
            users.add(new UserResponse(u.getUsername(),u.getName(),u.getAffiliation(),u.getEmail(),u.getWebpage()));
        ResponseJSON<Iterable<UserResponse>> resp = new ResponseJSON<>(users);
        return new ResponseEntity<>(resp,HttpStatus.OK);

    }

    @RequestMapping(path="/{sessionId}/sessionChair",method = RequestMethod.PUT)
    public ResponseEntity<ResponseJSON<String>> handle_put_sessionChair(
            @RequestBody UserIdRequest user,
            @PathVariable("sessionId") int sessionId)
    {
        ResponseJSON<String> resp=  new ResponseJSON<>();

        sessionService.updateSessionChair(sessionId, user.getId());
    }

}
