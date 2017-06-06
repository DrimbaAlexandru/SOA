package com.company.repository;

import com.company.domain.Session;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by sebi on 6/7/2017.
 */
@Repository
public interface SessionRepository extends PagingAndSortingRepository<Session, Integer> {
}
