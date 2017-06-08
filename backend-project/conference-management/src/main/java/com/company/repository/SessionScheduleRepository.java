package com.company.repository;

import com.company.domain.SessionSchedule;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Alex on 08.06.2017.
 */
@Repository
public interface SessionScheduleRepository extends PagingAndSortingRepository<SessionSchedule,Integer> {

}
