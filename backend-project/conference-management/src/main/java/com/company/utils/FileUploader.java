package com.company.utils;

import java.util.Optional;

/**
 * Created by AlexandruD on 03-Jun-17.
 */
public interface FileUploader {

    /**
     * Uploads a file to a storage unit
     * @param filePath The file path
     * @param data The file data
     * @return Optional.empty if an error occurs, an optional with the filename otherwise
     */
    Optional<String> uploadFile(String filePath, String data);
}
