package com.company.controller;

import com.company.controller.DTOs.ConferenceDTO;
import com.company.controller.DTOs.SessionDTO;
import com.company.controller.DTOs.UserResponse;
import com.company.domain.AppUser;
import com.company.controller.DTOs.utils_DTOs.SessionScheduleDTO;
import com.company.domain.Conference;
import com.company.domain.Session;
import com.company.service.ConferenceService;
import com.company.utils.ResponseJSON;
import com.company.utils.exception.Exceptional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by sebi on 6/7/2017.
 */

@RestController
@RequestMapping("/conferences")
public class ConferenceController {
    private ConferenceService conferenceService;

    public ConferenceController(
            @Autowired ConferenceService conferenceService) {
        this.conferenceService = conferenceService;
    }

    //tested (Date display issues)
    @RequestMapping(path = "", method = RequestMethod.GET)
    public ResponseEntity<ResponseJSON<Iterable<ConferenceDTO>>> handle_get_all()
    {
        List<ConferenceDTO> conferences=new ArrayList<>();
        for(Conference c:conferenceService.getConferences())
            conferences.add(new ConferenceDTO(c));
        ResponseJSON<Iterable<ConferenceDTO>> resp=new ResponseJSON<>(conferences);
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    //tested (Date display issues)
    @RequestMapping(path = "/{conferenceId}", method = RequestMethod.GET)
    public ResponseEntity<ResponseJSON<ConferenceDTO>> handle_get_conference(@PathVariable("conferenceId") Integer conferenceId){
        Exceptional<Conference> conferenceExceptional = conferenceService.getConference(conferenceId);
        ResponseJSON<ConferenceDTO> resp = new ResponseJSON<>();
        conferenceExceptional.error(e -> {resp.addError(e.getMessage());}).ok(
                file->{resp.setResp(new ConferenceDTO(file));}
        );
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @RequestMapping(path = "/{conferenceId}/PCMembers", method = RequestMethod.GET)
    public ResponseEntity<ResponseJSON<Iterable<UserResponse>>> handle_get_pc_memgers(
            @PathVariable("conferenceId") Integer confId) {

        Exceptional<Iterable<AppUser>> res =
                conferenceService.getPCMembers(confId);

        ResponseJSON<Iterable<UserResponse>> resp = new ResponseJSON<>();
        res.error(e -> {
            resp.addError(e.getMessage());
        }).ok(e -> {
            List<UserResponse> data =
                    StreamSupport.stream(e.spliterator(), false)
                        .map(UserResponse::new)
                        .collect(Collectors.toList());
            resp.setResp(data);
        });

        return ResponseEntity.ok(resp);
    }

    /** tested with structure:
    {
        "id": 7,
        "name": "post_test",
        "eventTimeSpan":{
            "startDate":1507582888000,
            "endDate":1507582888000
        },
        "callForAbstractTimeSpan":{
            "startDate":1507582888000,
            "endDate":1507582888000
        },
        "callForProposalsTimeSpan":{
            "startDate":1507582888000,
            "endDate":1507582888000
        },
        "biddingDeadline": 1507582888000
    }
     * @param conferenceDTO
     * @return 200
     */
    @RequestMapping(path = "", method = RequestMethod.POST)
    public ResponseEntity<ResponseJSON<String>> hendle_post_conference(@RequestBody ConferenceDTO conferenceDTO){
        ResponseJSON<String> resp = new ResponseJSON<>();
        conferenceService.addConference(conferenceDTO).error(e -> {resp.setResp(e.getMessage());});
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    /** tested with the structure  |^|
     */
    @RequestMapping(path = "/{conferenceId}", method = RequestMethod.PUT)
    public ResponseEntity<ResponseJSON<String>> handle_put_conference(
            @RequestBody ConferenceDTO conferenceDTO,
            @PathVariable("conferenceId") int conferenceId)
    {
        ResponseJSON<String> resp=new ResponseJSON<>();
        conferenceService.updateConference(conferenceId,conferenceDTO).error(e->{resp.addError(e.getMessage());});
        return new ResponseEntity<>(resp,HttpStatus.OK);
    }

    @RequestMapping(path = "/{conferenceId}/sessions", method = RequestMethod.GET)
    public ResponseEntity<ResponseJSON<Iterable<SessionDTO>>> hanlde_get_sessions(
            @PathVariable("conferenceId") int confId){
        List<SessionDTO> sessionDTOS = new ArrayList<>();
        Exceptional<Iterable<Session>> sessions = conferenceService.getSessions(confId);
        ResponseJSON<Iterable<SessionDTO>> responseJSON = new ResponseJSON<>();
        sessions.ok(sessions1 -> {
            for(Session s:sessions1){
                sessionDTOS.add(new SessionDTO(s));
            }
        }).error(e->{responseJSON.addError(e.getMessage());});
        responseJSON.setResp(sessionDTOS);
        return new ResponseEntity<>(responseJSON, HttpStatus.OK);
    }
}
