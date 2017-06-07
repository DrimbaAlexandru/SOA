package com.company.service;

import com.company.controller.DTOs.FileGetDTO;
import com.company.controller.DTOs.submittedPaperDTO;
import com.company.domain.AppUser;
import com.company.domain.Paper;
import com.company.domain.PaperStatus;
import com.company.domain.UploadedFile;
import com.company.utils.exception.Exceptional;

/**
 * Created by Alex on 07.06.2017.
 */
public interface PaperService {

    /**
     * @return Collection of all papers
     */
    Iterable<Paper> getAll();

    /**
     * @param p the paper to be inserted
     * @return Exceptional containing the newly inserted paper or an exception
     */
    Exceptional<Paper> addPaper(Paper p);

    /**
     * @param p the paper to be inserted
     * @return Exceptional containing the newly inserted paper or an exception
     */
    Exceptional<Paper> addPaper(submittedPaperDTO p);

    /**
     * @param p the paper to be replaced with
     * @param oldId the replaced paper's ID
     * @return Exceptional containing the newly updated paper or an exception
     */
    Exceptional<Paper> updatePaper(int oldId, Paper p);

    /**
     * @param p the paper to be replaced with
     * @param oldId the replaced paper's ID
     * @return Exceptional containing the newly updated paper or an exception
     */
    Exceptional<Paper> updatePaper(int oldId, submittedPaperDTO p);


    /**
     * @param paperId the paper which the file is added to
     * @param type document's extension
     * @param data document's raw bytes
     * @param fileType File's type regarding the paper (FULLPAPER, ABSTRACT, PRESENTATION)
     * @return Exceptional containing the newly uploaded UploadedFile or an exception
     */
    Exceptional<UploadedFile> addFile(int paperId, String type, byte[] data, String fileType);

    /**
     * @param status Paper status
     * @return Get all papers with given status
     */
    Iterable<Paper> getByStatus(String status);

    /**
     * @param paperId
     * @return Exceptional containing a collection of potential reviewers or an exception
     */
    Exceptional<Iterable<AppUser>> getPotentialReviewers(int paperId);

    /**
     * @param paperId
     * @return Exceptional containing a collection of assigned reviewers or an exception
     */
    Exceptional<Iterable<AppUser>> getAssignedReviewers(int paperId);

    /**
     * @param paperId
     * @param fileType File's type regarding the paper (FullPaper, Abstract, Presentation)
     * @return Latest file of the given type of the given paper
     */
    Exceptional<FileGetDTO> getFile(int paperId, String fileType);

    /**
     * @param paperId
     * @return exception if paperId is not k
     */
    Exceptional<Void> setPaperStatus(int paperId, PaperStatus status);

    /**
     * @param paperId
     * @return nothing, clear all reviews and sets the paper's status to WAITING_FOR_REVIEW
     */
    Exceptional<Void> reevaluatePaper(int paperId);

    /**
     * @param paperId
     * @param username
     * @return Clears all the reviews, sets the paper's status to WAITING_FOR_REVIEW
     * and designates a reviewer to give the final review or exception
     */
    Exceptional<Void> setFinalEvaluator(int paperId, String username);

}
