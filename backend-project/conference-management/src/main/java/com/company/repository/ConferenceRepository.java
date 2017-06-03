package com.company.repository;

import com.company.domain.Conference;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by AlexandruD on 03-Jun-17.
 */
@Repository
public interface ConferenceRepository extends PagingAndSortingRepository<Conference, Integer> {
}
