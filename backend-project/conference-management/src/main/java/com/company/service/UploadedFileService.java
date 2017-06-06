package com.company.service;

import com.company.domain.UploadedFile;
import com.company.utils.exception.Exceptional;

import java.util.Optional;

/**
 * Created by AlexandruD on 03-Jun-17.
 */
public interface UploadedFileService {

    /**
     * Uploads a file to a storage component
     * @param path The file path
     * @param data The data
     * @return The file's metadata if upload succeeds. Optional.empty() otherwise
     */
    Exceptional<UploadedFile> uploadFile(String path, byte[] data);

    Exceptional<Void> saveUploadedFileData(UploadedFile data);

}
