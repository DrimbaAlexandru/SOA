package com.company.service;

import com.company.controller.DTOs.FileGetDTO;
import com.company.controller.DTOs.firstPaperSubmissionDTO;
import com.company.controller.DTOs.submittedPaperDTO;
import com.company.domain.*;
import com.company.repository.*;
import com.company.utils.container.Container;
import com.company.utils.exception.Exceptional;
import com.company.utils.updater.PapersGettersAndSetters;
import com.company.utils.updater.Updater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.*;

/**
 * Created by David on 6/5/2017.
 */

@Component
public class PaperServiceImpl implements PaperService {

    private PaperRepository paperRepository;
    private PapersGettersAndSetters papersGettersAndSetters;
    private Updater updater;
    private UploadedFileService uploadedFileService;
    private UploadedFileRepository uploadedFileRepository;
    private UserRepository userRepository;
    private ConferenceRepository conferenceRepository;
    private SessionRepository sessionRepository;
    private SessionScheduleRepository sessionScheduleRepository;

    public PaperServiceImpl(@Autowired PaperRepository paperRepository,
                            @Autowired PapersGettersAndSetters papersGettersAndSetters,
                            @Autowired UploadedFileService uploadedFileService,
                            @Autowired UserRepository userRepository,
                            @Autowired UploadedFileRepository uploadedFileRepository,
                            @Autowired ConferenceRepository conferenceRepository,
                            @Autowired SessionRepository sessionRepository,
                            @Autowired SessionScheduleRepository sessionScheduleRepository){
        this.paperRepository=paperRepository;
        this.papersGettersAndSetters=papersGettersAndSetters;
        updater=new Updater();
        this.uploadedFileService = uploadedFileService;
        this.userRepository=userRepository;
        this.uploadedFileRepository=uploadedFileRepository;
        this.conferenceRepository=conferenceRepository;
        this.sessionRepository=sessionRepository;
        this.sessionScheduleRepository=sessionScheduleRepository;
    }

    @Override
    public Iterable<Paper> getAll()
    {
        return paperRepository.findAll();
    }

    public Exceptional<Paper> addPaper(Paper p)
    {
        return Exceptional.OK(paperRepository.save(p));
    }

    @Transactional
    public Exceptional<Paper> addPaper(firstPaperSubmissionDTO p)
    {
        Set<AppUser> users=new HashSet<>();
        Conference conference=conferenceRepository.findOne(p.getConferenceId());
        if(conference==null)
            return Exceptional.Error(new Exception("I'm not in the mood to execute this task. Please use the keyword 'PLEASE' or put in a valid conference ID"));

        Paper paper=new Paper(p.getName(),PaperStatus.SUBMITTED);
        paper = paperRepository.save(paper);

        for(String username:p.getAuthors())
        {
            AppUser u = userRepository.findByUsername(username);
            if(u!=null)
            {
                users.add(u);
                u.getSubmittedPapers().add(paper);
            }
        }
        if(users.size()==0)
            return Exceptional.Error(new Exception("No correct usernames given as authors"));


        Set<Session> sessions=conference.getSessions();
        Session session;
        SessionSchedule sessionSchedule;
        if(sessions.size()==0) {
            session = new Session(conference, "Default Session");
            session = sessionRepository.save(session);
        }
        else
            session = (Session)sessions.toArray()[0];

        System.out.println(session.getName());

        paper = paperRepository.save(paper);
        sessionSchedule = new SessionSchedule(paper,session,new Date(),(AppUser) users.toArray()[0]);
        sessionScheduleRepository.save(sessionSchedule);
        return Exceptional.OK(paper);
    }

    public Exceptional<Paper> updatePaper(int oldId, Paper p)
    {
        Paper old=paperRepository.findOne(oldId);
        if(old==null)
            return Exceptional.Error(new Exception("Paper with given id doesn't exist"));
        else
        {
            p.setId(null);
            updater.<Paper>update(old,p,papersGettersAndSetters.getGettersAndSetters());
            this.paperRepository.save(old);
            return Exceptional.OK(old);
        }
    }

    public Exceptional<Paper> updatePaper(int oldId, submittedPaperDTO p)
    {
        Paper old=paperRepository.findOne(oldId);
        if(old==null)
            return Exceptional.Error(new Exception("Paper with given id doesn't exist"));
        else
        {
            Set<AppUser> users=new HashSet<>();
            Paper paper=new Paper(p.getName(),PaperStatus.SUBMITTED);
            old.getAuthors().clear();
            for(String username:p.getAuthors())
            {
                AppUser u = userRepository.findByUsername(username);
                if(u!=null)
                {
                    users.add(u);
                    u.getSubmittedPapers().add(old);
                }
            }
            paper.setId(null);
            updater.<Paper>update(old,paper,papersGettersAndSetters.getGettersAndSetters());
            this.paperRepository.save(old);
            return Exceptional.OK(old);
        }
    }

    @Transactional
    public Exceptional<UploadedFile> addFile(int paperId, String type, byte[] data, String fileType)
    {
        Paper paper=paperRepository.findOne(paperId);
        if(paper==null)
            return Exceptional.Error(new Exception("Paper with given id doesn't exist"));
        String filePath = "/" + paperId + new Date().toString();
        Exceptional<UploadedFile> uf = uploadedFileService.uploadFile(filePath+"."+type,data);
        Container<Exceptional<UploadedFile>> container=new Container<>(uf);
        uf.ok(file -> {
            switch (fileType)
            {
                case "FULLPAPER": paper.getFullPapers().add(file); file.setPaperIsFullPaperFor(paper); paperRepository.save(paper);break;
                case "ABSTRACT": paper.getAbstracts().add(file); file.setPaperIsAbstractFor(paper); paperRepository.save(paper); break;
                case "PRESENTATION":
                    UploadedFile f = paper.getPresentation();

                    paper.setPresentation(file);
                    file.setPaperIsPresentationFor(paper);
                    paperRepository.save(paper);
                    if(f!=null)
                        uploadedFileRepository.delete(f.getId());
                    break;
                default: container.setValue(Exceptional.Error(new Exception("Unsupported file type "+fileType)));
            }
        }).error(e->{container.setValue(Exceptional.Error(e));});
        return container.getValue();
    }


    public Iterable<Paper> getByStatus(String status)
    {
        List<Paper> papers=new ArrayList<>();
        paperRepository.findAll().forEach(p->{if(p.getStatus().name().equals(status)) papers.add(p);});
        return papers;
    }
    @Transactional
    public Exceptional<Iterable<AppUser>> getPotentialReviewers(int paperId)
    {
        List<AppUser> potentialReviewers=new ArrayList<>();
        Paper p=paperRepository.findOne(paperId);
        if(p==null)
            return Exceptional.Error(new Exception("Paper with given id doesn't exist"));
        for(Bid b:p.getBids())
        {
            if(b.getStatus()!=BidStatus.REJECT)
                potentialReviewers.add(b.getBidder());
        }
        return Exceptional.OK(potentialReviewers);
    }
    @Transactional
    public Exceptional<Iterable<AppUser>> getAssignedReviewers(int paperId)
    {
        Paper p=paperRepository.findOne(paperId);
        if(p==null)
            return Exceptional.Error(new Exception("Paper with given id doesn't exist"));
        return Exceptional.OK(p.getReviewers());
    }
    @Transactional
    public Exceptional<FileGetDTO> getFile(int paperId, String fileType)
    {
        Paper paper=paperRepository.findOne(paperId);
        if(paper==null)
            return Exceptional.Error(new Exception("Paper with given id doesn't exist"));

        UploadedFile actualFile=null;
        switch (fileType)
        {
            case "FULLPAPER":{
                UploadedFile uf=null;
                for(UploadedFile file:paper.getFullPapers())
                {
                    if(uf==null || file.getDateUploaded().compareTo(uf.getDateUploaded())>0)
                        uf=file;
                }
                actualFile=uf;
                break;
                }
            case "ABSTRACT":{
                UploadedFile uf=null;
                for(UploadedFile file:paper.getAbstracts())
                {
                    if(uf==null || file.getDateUploaded().compareTo(uf.getDateUploaded())>0)
                        uf=file;
                }
                actualFile=uf;
                break;
            }
            case "PRESENTATION": actualFile=paper.getPresentation();break;
            default: return (Exceptional.Error(new Exception("Unsupported file type "+fileType)));
        }
        if(actualFile!=null)
            return uploadedFileService.getFileData(actualFile);
        else
            return (Exceptional.Error(new Exception("There is not a single fucking paper like this")));
    }



    public Exceptional<Void> setPaperStatus(int paperId, PaperStatus status)
    {
        Paper paper=paperRepository.findOne(paperId);
        if(paper==null)
            return Exceptional.Error(new Exception("Paper with given id doesn't exist"));

        paper.setStatus(status);
        paperRepository.save(paper);
        return Exceptional.OK(null);
    }
    @Transactional
    public Exceptional<Void> setFinalEvaluator(int paperId, String username)
    {
        Paper paper=paperRepository.findOne(paperId);
        if(paper==null)
            return Exceptional.Error(new Exception("Paper with given id doesn't exist"));
        AppUser user=userRepository.findByUsername(username);
        if(user==null)
            return Exceptional.Error(new Exception("Username does not exist"));
        if(paper.getStatus()!=PaperStatus.CONTRADICTORY_REVIEW)
            return Exceptional.Error(new Exception("Paper is not in a contradictory review state"));
        paper.getReviews().clear();
        setPaperStatus(paperId,PaperStatus.WAITING_FOR_REVIEW);
        paper.getReviewers().clear();
        paper.getReviewers().add(user);
        user.getAssignedForReview().add(paper);
        paperRepository.save(paper);
        userRepository.save(user);
        return Exceptional.OK(null);
    }
    @Transactional
    public Exceptional<Void> reevaluatePaper(int paperId)
    {
        Paper paper=paperRepository.findOne(paperId);
        if(paper==null)
            return Exceptional.Error(new Exception("Paper with given id doesn't exist"));
        if(paper.getStatus()!=PaperStatus.CONTRADICTORY_REVIEW)
            return Exceptional.Error(new Exception("Paper is not in a contradictory review state"));

        //paper.setReviews(new HashSet<>(0));

        paper.getReviews().clear();

        setPaperStatus(paperId,PaperStatus.WAITING_FOR_REVIEW);
        paperRepository.save(paper);
        return Exceptional.OK(null);
    }

    @Override
    public Exceptional<Paper> getById(int id) {
        Paper p=paperRepository.findOne(id);
        if(p==null)
            return Exceptional.Error(new Exception("Paper with given id doesn't exist"));
        else
            return Exceptional.OK(p);
    }

    @Transactional
    @Override
    public Exceptional<Iterable<Review>> getReviews(int id) {
        Paper p=paperRepository.findOne(id);
        if(p==null)
            return Exceptional.Error(new Exception("Paper with given id doesn't exist"));
        else
            return Exceptional.OK(p.getReviews());
    }

    @Transactional
    @Override
    public Exceptional<Iterable<Bid>> getBids(int id) {
        Paper p=paperRepository.findOne(id);
        if(p==null)
            return Exceptional.Error(new Exception("Paper with given id doesn't exist"));
        else
            return Exceptional.OK(p.getBids());
    }
}
