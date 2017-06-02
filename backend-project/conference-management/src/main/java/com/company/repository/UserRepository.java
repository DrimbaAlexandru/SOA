package com.company.repository;

import com.company.domain.AppUser;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by AlexandruD on 02-Jun-17.
 */
@Repository
public interface UserRepository extends PagingAndSortingRepository<AppUser, String> {

    AppUser findByUsername(String username);
}
