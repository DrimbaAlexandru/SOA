package com.company.controller;

import com.company.controller.DTOs.*;
import com.company.domain.*;
import com.company.service.PaperService;
import com.company.service.PaperServiceImpl;
import com.company.utils.ResponseJSON;
import com.company.utils.exception.Exceptional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 6/5/2017.
 */
@RestController
@RequestMapping("/papers")
public class PaperController {

    private PaperService service;

    public PaperController(@Autowired PaperService servicePaper){
        this.service=servicePaper;
    }

    //TESTED
    @RequestMapping(path = "", method = RequestMethod.GET)
    public ResponseEntity<ResponseJSON<Iterable<submittedPaperDTO>>> handle_get_all()
    {
        List<submittedPaperDTO> papers=new ArrayList<>();
        for(Paper p:service.getAll())
            papers.add(new submittedPaperDTO(p));
        ResponseJSON<Iterable<submittedPaperDTO>> resp=new ResponseJSON<>(papers);
        return new ResponseEntity<>(resp,HttpStatus.OK);
    }

    //TESTED
    @RequestMapping(path = "/{paperId}", method = RequestMethod.GET)
    public ResponseEntity<ResponseJSON<submittedPaperDTO>> handle_get_paper_by_id(
            @PathVariable("paperId") int paperId)
    {
        ResponseJSON<submittedPaperDTO> resp=new ResponseJSON<>();
        service.getById(paperId).error(e->{resp.addError(e.getMessage());}).ok(
            p->{resp.setResp(new submittedPaperDTO(p));}
        );
        return new ResponseEntity<>(resp,HttpStatus.OK);
    }

    //TESTED
    @RequestMapping(path = "", method = RequestMethod.POST)
    public ResponseEntity<ResponseJSON<String>> handle_post_paper(
            @RequestBody firstPaperSubmissionDTO paper)
    {
        ResponseJSON<String> resp=new ResponseJSON<>();
        service.addPaper(paper).error(e->{resp.addError(e.getMessage());});
        return new ResponseEntity<ResponseJSON<String>>(resp,HttpStatus.OK);
    }

    //TESTED
    @RequestMapping(path = "/{paperID}", method = RequestMethod.PUT)
    public ResponseEntity<ResponseJSON<String>> handle_put_paper(
            @RequestBody submittedPaperDTO paper,
            @PathVariable("paperID") int paperId)
    {
        ResponseJSON<String> resp=new ResponseJSON<>();
        service.updatePaper(paperId,paper).error(e->{resp.addError(e.getMessage());});
        return new ResponseEntity<ResponseJSON<String>>(resp,HttpStatus.OK);
    }

    //TESTED
    @RequestMapping(path = "/acceptedProposals", method = RequestMethod.GET)
    public ResponseEntity<ResponseJSON<Iterable<submittedPaperDTO>>> handle_get_accepted()
    {
        List<submittedPaperDTO> papers=new ArrayList<>();
        for(Paper p:service.getByStatus("ACCEPTED"))
            papers.add(new submittedPaperDTO(p));
        ResponseJSON<Iterable<submittedPaperDTO>> resp=new ResponseJSON<>(papers);
        return new ResponseEntity<>(resp,HttpStatus.OK);
    }

    //TESTED
    @RequestMapping(path = "/{paperId}/potentialReviewers", method = RequestMethod.GET)
    public ResponseEntity<ResponseJSON<Iterable<updateUserRequest>>> handle_get_potentialReviewers(
            @PathVariable("paperId") int paperId)
    {
        List<updateUserRequest> users=new ArrayList<>();
        Exceptional<Iterable<AppUser>> reviewers = service.getPotentialReviewers(paperId);
        ResponseJSON<Iterable<updateUserRequest>> resp=new ResponseJSON<>();

        reviewers.ok(revs->{
            for(AppUser u:revs) {
                updateUserRequest request=new updateUserRequest();
                request.setUsername(u.getUsername());
                users.add(request);
            }
        }).error(e->{resp.addError(e.getMessage());});
        resp.setResp(users);
        return new ResponseEntity<>(resp,HttpStatus.OK);
    }

    //TESTED
    @RequestMapping(path = "/{paperId}/reviews", method = RequestMethod.GET)
    public ResponseEntity<ResponseJSON<Iterable<reviewDTO>>> handle_get_reviews(
            @PathVariable("paperId") int paperId)
    {
        ResponseJSON<Iterable<reviewDTO>> resp=new ResponseJSON<>();

        List<reviewDTO> reviews=new ArrayList<>();
        Exceptional<Iterable<Review>> r =service.getReviews(paperId);
        r.error(e->{resp.addError(e.getMessage());}).ok(
            revs->{
                for(Review rev : revs)
                    reviews.add(new reviewDTO(rev));
            }
        );
        resp.setResp(reviews);
        return new ResponseEntity<ResponseJSON<Iterable<reviewDTO>>>(resp,HttpStatus.OK);
    }

    //TESTED
    @RequestMapping(path = "/{paperId}/bids", method = RequestMethod.GET)
    public ResponseEntity<ResponseJSON<Iterable<bidDTO>>> handle_get_bids(
            @PathVariable("paperId") int paperId)
    {
        ResponseJSON<Iterable<bidDTO>> resp=new ResponseJSON<>();

        List<bidDTO> bids=new ArrayList<>();
        Exceptional<Iterable<Bid>> excbids =service.getBids(paperId);
        excbids.error(e->{resp.addError(e.getMessage());}).ok(
                bids1->{
                    for(Bid b : bids1)
                        bids.add(new bidDTO(b));
                }
        );
        resp.setResp(bids);
        return new ResponseEntity<ResponseJSON<Iterable<bidDTO>>>(resp,HttpStatus.OK);
    }

    //TESTED
    @RequestMapping(path = "/{paperId}/assignedReviewers", method = RequestMethod.GET)
    public ResponseEntity<ResponseJSON<Iterable<updateUserRequest>>> handle_get_assignedReviewers(
            @PathVariable("paperId") int paperId)
    {
        List<updateUserRequest> users=new ArrayList<>();
        Exceptional<Iterable<AppUser>> reviewers = service.getAssignedReviewers(paperId);
        ResponseJSON<Iterable<updateUserRequest>> resp=new ResponseJSON<>();

        reviewers.ok(revs->{
            for(AppUser u:revs) {
                updateUserRequest request=new updateUserRequest();
                request.setUsername(u.getUsername());
                users.add(request);
            }
        }).error(e->{resp.addError(e.getMessage());});
        resp.setResp(users);
        return new ResponseEntity<>(resp,HttpStatus.OK);
    }

    //TESTED
    @RequestMapping(path = "/{paperId}/reevaluate", method = RequestMethod.PUT)
    public ResponseEntity<ResponseJSON<Void>> handle_reevaluate(
            @PathVariable("paperId") int paperId)
    {
        ResponseJSON<Void> resp=new ResponseJSON<>(null);
        service.reevaluatePaper(paperId).error(e->{resp.addError(e.getMessage());});
        return new ResponseEntity<ResponseJSON<Void>>(resp,HttpStatus.OK);
    }

    //TESTED
    @RequestMapping(path = "/{paperId}/finalEvaluator/{userId}", method = RequestMethod.PUT)
    public ResponseEntity<ResponseJSON<Void>> handle_finalEvaluator(
            @PathVariable("paperId") int paperId,
            @PathVariable("userId") String username)
    {
        ResponseJSON<Void> resp=new ResponseJSON<>(null);
        service.setFinalEvaluator(paperId,username).error(e->{resp.addError(e.getMessage());});
        return new ResponseEntity<ResponseJSON<Void>>(resp,HttpStatus.OK);
    }

    //TESTED
    @RequestMapping(path = "/{paperId}/full",
            method = RequestMethod.POST)
    public ResponseEntity<ResponseJSON<String>> uploadFullFile(
            @PathVariable("paperId") Integer paperId,
            @RequestParam("type") String type,
            @RequestParam("file") MultipartFile file) {

        ResponseJSON<String> resp = new ResponseJSON<>();
        try {
            byte[] fileData = file.getBytes();

            Exceptional<UploadedFile> retur = service.addFile(paperId,type,fileData,"FULLPAPER");
            retur.error(e->{resp.addError(e.getMessage());});
            return new ResponseEntity<ResponseJSON<String>>(resp, HttpStatus.OK);
        }
        catch(IOException e) {
            resp.addError("Error occured on file upload");
            return new ResponseEntity<ResponseJSON<String>>(resp,HttpStatus.OK);
        }
    }

    //TESTED
    @RequestMapping(path = "/{paperId}/abstract",
            method = RequestMethod.POST)
    public ResponseEntity<ResponseJSON<String>> uploadAbstract(
            @PathVariable("paperId") Integer paperId,
            @RequestParam("type") String type,
            @RequestParam("file") MultipartFile file) {

        ResponseJSON<String> resp = new ResponseJSON<>();
        try {
            byte[] fileData = file.getBytes();

            Exceptional<UploadedFile> retur = service.addFile(paperId,type,fileData,"ABSTRACT");
            retur.error(e->{resp.addError(e.getMessage());});
            return new ResponseEntity<ResponseJSON<String>>(resp, HttpStatus.OK);
        }catch(IOException e) {
            resp.addError("Error occured on file upload");
            return new ResponseEntity<ResponseJSON<String>>(resp,HttpStatus.OK);
        }
    }

    //TESTED
    @RequestMapping(path = "/{paperId}/presentation",
            method = RequestMethod.POST)
    public ResponseEntity<ResponseJSON<String>> uploadPresentation(
            @PathVariable("paperId") Integer paperId,
            @RequestParam("type") String type,
            @RequestParam("file") MultipartFile file) {

        ResponseJSON<String> resp = new ResponseJSON<>();
        try {
            byte[] fileData = file.getBytes();

            Exceptional<UploadedFile> retur = service.addFile(paperId,type,fileData,"PRESENTATION");
            retur.error(e->{resp.addError(e.getMessage());});
            return new ResponseEntity<ResponseJSON<String>>(resp, HttpStatus.OK);
        }catch(IOException e) {
            resp.addError("Error occured on file upload");
            return new ResponseEntity<ResponseJSON<String>>(resp,HttpStatus.OK);
        }
    }
    //TESTED
    @RequestMapping(path = "/{paperId}/full",method = RequestMethod.GET)
    public ResponseEntity<ResponseJSON<FileGetDTO>> getFullFile(
            @PathVariable("paperId") Integer paperId) {
        ResponseJSON<FileGetDTO> resp = new ResponseJSON<>();
        Exceptional<FileGetDTO> uf = service.getFile(paperId,"FULLPAPER");
        uf.error(e->{resp.addError(e.getMessage());}).ok(
            file->{
                resp.setResp(file);
            }
        );
        return new ResponseEntity<ResponseJSON<FileGetDTO>>(resp,HttpStatus.OK);
    }

    //TESTED
    @RequestMapping(path = "/{paperId}/abstract",method = RequestMethod.GET)
    public ResponseEntity<ResponseJSON<FileGetDTO>> getAbstractFile(
            @PathVariable("paperId") Integer paperId) {
        ResponseJSON<FileGetDTO> resp = new ResponseJSON<>();
        Exceptional<FileGetDTO> uf = service.getFile(paperId,"ABSTRACT");
        uf.error(e->{resp.addError(e.getMessage());}).ok(
                file->{
                    resp.setResp(file);
                }
        );
        return new ResponseEntity<ResponseJSON<FileGetDTO>>(resp,HttpStatus.OK);
    }
    //TESTED
    @RequestMapping(path = "/{paperId}/presentation",method = RequestMethod.GET)
    public ResponseEntity<ResponseJSON<FileGetDTO>> getPresentation(
            @PathVariable("paperId") Integer paperId) {
        ResponseJSON<FileGetDTO> resp = new ResponseJSON<>();
        Exceptional<FileGetDTO> uf = service.getFile(paperId,"PRESENTATION");
        uf.error(e->{resp.addError(e.getMessage());}).ok(
                file->{
                    resp.setResp(file);
                }
        );
        return new ResponseEntity<ResponseJSON<FileGetDTO>>(resp,HttpStatus.OK);
    }
}
