package com.company.controller;

import com.company.domain.AppUser;
import com.company.domain.Paper;
import com.company.domain.Review;
import com.company.service.PaperService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by David on 6/5/2017.
 */
@RestController
@RequestMapping("/Paper")
public class PaperController {

    private PaperService servicePaper;

    public PaperController(@Autowired PaperService servicePaper){
        this.servicePaper=servicePaper;
    }

    @RequestMapping(method=RequestMethod.GET)
    public ResponseEntity<Iterable<Paper>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(servicePaper.getAll());
    }

    @RequestMapping(value ="/AcceptedProporsal", method=RequestMethod.GET)
    public ResponseEntity<Iterable<Paper>> getAcceptedProporsal() {
        return ResponseEntity.status(HttpStatus.OK).body(servicePaper.getAcceptedProporsal());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Paper> getPaper(@PathVariable("id") Integer id) {
        Paper paper = servicePaper.getPaper(id);
        if(paper != null) {
            return ResponseEntity.status(HttpStatus.OK).body(paper);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @RequestMapping(value = "/Reviewer/{id}", method = RequestMethod.GET)
    public ResponseEntity<Iterable<AppUser>> getPotentialReviewrs(@PathVariable("id") Integer id) {
            if(servicePaper.getPotentialReviewrs(id)!=null) {
                return ResponseEntity.status(HttpStatus.OK).body(servicePaper.getPotentialReviewrs(id));
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
    }
}
