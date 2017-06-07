package com.company.repository;

import com.company.domain.UploadedFile;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by AlexandruD on 03-Jun-17.
 */
@Repository
public interface UploadedFileRepository extends PagingAndSortingRepository<UploadedFile, Integer>{
}
