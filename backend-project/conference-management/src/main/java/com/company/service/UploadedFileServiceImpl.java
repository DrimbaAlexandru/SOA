package com.company.service;

import com.company.controller.DTOs.FileGetDTO;
import com.company.controller.DTOs.FilePostDTO;
import com.company.domain.UploadedFile;
import com.company.repository.UploadedFileRepository;
import com.company.utils.RemoteFileManager;
import com.company.utils.exception.Exceptional;
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
    public Exceptional<UploadedFile> uploadFile(String path, byte[] data) {
        Optional<String> filename = uploader.uploadFile(path, data);

        if(!filename.isPresent()) {
            return Exceptional.Error(new Exception("Error occured when uploading file"));
        }

        UploadedFile file = new UploadedFile(filename.get(), filename.get(), new Date());
        file = repo.save(file);

        return Exceptional.OK(file);
    }

    @Override
    public Exceptional<Void> saveUploadedFileData(UploadedFile file) {
        repo.save(file);
        return Exceptional.OK(null);
    }

    public Exceptional<FileGetDTO> getFileData(UploadedFile file)
    {
        Optional<byte[]> data =uploader.getFileData("/"+file.getFilePath());
        if(data.isPresent())
            return Exceptional.OK(new FileGetDTO(data.get()));
        else
            return Exceptional.Error(new Exception("Operation failed"));
    }

}
