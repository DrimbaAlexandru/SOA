package com.company.service;

import com.company.domain.UploadedFile;
import com.company.repository.UploadedFileRepository;
import com.company.utils.RemoteFileManager;
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
    private RemoteFileManager uploader;

    public UploadedFileServiceImpl(@Autowired UploadedFileRepository repo,
                                   @Autowired RemoteFileManager uploader) {
        this.repo = repo;
        this.uploader = uploader;
    }

    @Override
    public Optional<UploadedFile> uploadFile(String path, byte[] data) {
        Optional<String> filename = uploader.uploadFile(path, data);

        if(!filename.isPresent()) {
            return Optional.empty();
        }

        UploadedFile file = new UploadedFile(filename.get(), filename.get(), new Date());
        file = repo.save(file);

        return Optional.of(file);
    }

    @Override
    public void saveUploadedFileData(UploadedFile file) {
        repo.save(file);
    }

}
