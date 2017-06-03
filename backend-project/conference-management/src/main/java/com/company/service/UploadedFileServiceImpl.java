package com.company.service;

import com.company.domain.UploadedFile;
import com.company.repository.UploadedFileRepository;
import com.company.utils.FileUploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

/**
 * Created by AlexandruD on 03-Jun-17.
 */
@Component
public class UploadedFileServiceImpl implements UploadedFileService{

    private UploadedFileRepository repo;
    private FileUploader uploader;

    public UploadedFileServiceImpl(@Autowired UploadedFileRepository repo,
                                   @Autowired FileUploader uploader) {
        this.repo = repo;
        this.uploader = uploader;
    }

    public Optional<UploadedFile> uploadFile(String path, String data) {
        Optional<String> filename = uploader.uploadFile(path, data);

        if(!filename.isPresent()) {
            return Optional.empty();
        }

        UploadedFile file = new UploadedFile(filename.get(), filename.get(), new Date());
        file = repo.save(file);

        return Optional.of(file);
    }

    public void saveUploadedFileData(UploadedFile file) {
        repo.save(file);
    }

}
