package com.company.utils.dropbox;

import com.company.utils.FileUploader;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.UploadUploader;

import java.io.PrintWriter;
import java.util.Optional;

/**
 * Created by AlexandruD on 03-Jun-17.
 */
public class DropboxUploader implements FileUploader {

    private DbxClientV2 client;
    private String key, secret;

    public DropboxUploader(String key, String secret) {
        this.key = key;
        this.secret = secret;
        client = new DbxClientV2(DbxRequestConfig.newBuilder("dropbox/Conference-Management").build(), key);
    }

    public Optional<String> uploadFile(String fileData, String fileName) {
        try {
            UploadUploader uploader = client.files().upload(fileName);
            PrintWriter writer = new PrintWriter(uploader.getOutputStream());
            writer.write(fileData);
            writer.flush();
            FileMetadata data = uploader.finish();
            return Optional.of(data.getName());
        }catch(DbxException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

}
