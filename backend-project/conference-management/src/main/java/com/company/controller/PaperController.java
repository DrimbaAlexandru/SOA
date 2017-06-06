package com.company.controller;

import com.company.domain.AppUser;
import com.company.domain.Paper;
import com.company.service.PaperServiceImpl;
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

    private PaperServiceImpl servicePaper;

    public PaperController(@Autowired PaperServiceImpl servicePaper){
        this.servicePaper=servicePaper;
    }


}
