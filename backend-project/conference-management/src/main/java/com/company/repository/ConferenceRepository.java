package com.company.repository;

import com.company.domain.AppUser;
import com.company.domain.Conference;
import com.company.domain.Session;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by AlexandruD on 03-Jun-17.
 */
@Repository
public interface ConferenceRepository extends PagingAndSortingRepository<Conference, Integer> {

    @Query("SELECT co.sessions FROM Conference co where co.id = ?1")
    Iterable<Session> getSessions(int confId);

    @Query("SELECT u FROM Conference co JOIN Privileges priv JOIN User u where co.id = ?1 AND priv.isPCMember = true")
    Iterable<AppUser> getPCMembers(int confId);

    @Query("SELECT u FROM Conference co JOIN Privileges priv JOIN User u where co.id = ?1")
    Iterable<AppUser> getRegisteredUsers(int confId);
}
