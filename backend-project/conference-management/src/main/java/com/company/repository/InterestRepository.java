package com.company.repository;

import com.company.domain.SubjectOfInterest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterestRepository extends PagingAndSortingRepository< SubjectOfInterest, Integer >
{
    SubjectOfInterest findBySearchStringAndOwnerId( String searchString, Integer ownerId );
}
