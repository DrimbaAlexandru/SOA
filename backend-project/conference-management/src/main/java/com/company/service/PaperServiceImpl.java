package com.company.service;

import com.company.domain.AppUser;
import com.company.domain.Paper;
import com.company.domain.Review;
import com.company.repository.PaperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by David on 6/5/2017.
 */

@Component
public class PaperServiceImpl implements PaperService {

    private PaperRepository repository;


    public PaperServiceImpl(@Autowired PaperRepository repository){
        this.repository=repository;
    }


}
