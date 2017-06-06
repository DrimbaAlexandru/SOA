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
public class PaperService {

    private PaperRepository repository;


    public PaperService(@Autowired PaperRepository repository){
        this.repository=repository;
    }

    public Paper getPaper(Integer id){
        return repository.findOne(id);
    }

    public Paper addPaper(Paper p){
        repository.save(p);
        return p;
    }

    public Iterable<Paper> getAll(){
        return repository.findAll();
    }

    public Iterable<Paper> getPapersConference(Integer conferenceId){
        return repository.findAll();
    }

    public void UploadFullPaperFile(Integer id){

        repository.findOne(id).setFullPapers(null);
    }

    public Set<AppUser> getPotentialReviewrs(Integer id){
        Set<Review> reviews=repository.findOne(id).getReviews();
        Set<AppUser> users=new HashSet<>();
        for(Review r :reviews){
            users.add(r.getReviewer());
        }
        return users;
    }

    public Set<Review> getAssignedReviewrs(Integer id){
        //Set<Review> reviews=repository.findOne(id).
        return null;
    }

    public Set<Paper> getAcceptedProporsal(){
        Set<Paper> papers=new HashSet<>();
        for(Paper paper:repository.findAll()){
            if(paper.getStatus().getNume().compareTo("Accepted")==0){
                papers.add(paper);
            }
        }
        return papers;
    }

    public void setFinalEvaluator(Integer id,Integer idEval){
        //repository.findOne(id).
    }




}
