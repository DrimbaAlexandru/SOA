package com.company.repository;

import com.company.domain.AppUser;
import com.company.domain.Bid;
import com.company.domain.Paper;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by AlexandruD on 02-Jun-17.
 */
@Repository
public interface UserRepository extends PagingAndSortingRepository<AppUser, String> {

    AppUser findByUsername(String username);

    @Query("SELECT au.submittedPapers FROM AppUser au where au.username = ?1")
    Iterable<Paper> getSubmittedPapers(String username);

    @Query("SELECT au.assignedForReview FROM AppUser au where au.username = ?1")
    Iterable<Paper> getAssignedPapers(String username);

    @Query("SELECT b FROM AppUser au JOIN au.bids b where au.username = ?1 and b.paper.id = ?2")
    Bid getBidOfPaper(String username, Integer paperId);

    @Query("SELECT CASE WHEN (count(*) > 0) then True else False end FROM AppUser au WHERE au.username = ?1")
    boolean userExists(String username);
}
