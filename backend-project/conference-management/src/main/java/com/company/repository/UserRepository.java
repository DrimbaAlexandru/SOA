package com.company.repository;

import com.company.domain.AppUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends PagingAndSortingRepository<AppUser, Integer> {

    AppUser findByUsername(String username);

    @Query("SELECT CASE WHEN (count(*) > 0) then True else False end FROM AppUser au WHERE au.username = ?1")
    boolean userExists(String username);
}
