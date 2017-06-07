package com.company.controller;

import com.company.controller.DTOs.ConferenceDTO;
import com.company.domain.Conference;
import com.company.service.ConferenceService;
import com.company.utils.ResponseJSON;
import com.company.utils.exception.Exceptional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
    @RequestMapping(path = "/", method = RequestMethod.GET)
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

    //not working properly...  "exception": "org.springframework.http.converter.HttpMessageNotReadableException"
    @RequestMapping(path = "/", method = RequestMethod.POST)
    public ResponseEntity<ResponseJSON<String>> hendle_post_conference(@RequestBody ConferenceDTO conferenceDTO){
        ResponseJSON<String> resp = new ResponseJSON<>();
        conferenceService.addConference(conferenceDTO).error(e -> {resp.setResp(e.getMessage());});

        return new ResponseEntity<>(resp, HttpStatus.OK);
    }



}
