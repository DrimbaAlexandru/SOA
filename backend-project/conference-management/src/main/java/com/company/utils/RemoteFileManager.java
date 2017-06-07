package com.company.utils;

import com.company.domain.UploadedFile;

import java.util.Optional;

/**
 * Created by AlexandruD on 03-Jun-17.
 */
public interface RemoteFileManager {

    /**
     * Uploads a file to a storage unit
     * @param filePath The file path
     * @param data The file data
     * @return Optional.empty if an error occurs, an optional with the filename otherwise
     */
    Optional<String> uploadFile(String filePath, byte[] data);

    /**
     * Obtains a file from the storage unit
     * @param filePath The file path
     * @return Optional.empty if an error occurs, an optional with the filename otherwise
     */
    Optional<byte[]> getFileData(String filePath);
}
