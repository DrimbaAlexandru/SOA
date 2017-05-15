package com.company.repository;

import com.company.domain.TestEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by AlexandruD on 15-May-17.
 */
@Repository
public interface TestEntityRepository extends PagingAndSortingRepository<TestEntity, Integer> {
}
