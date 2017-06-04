package com.company.utils.dropbox;

import com.company.utils.RemoteFileManager;
import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.UploadUploader;

import java.io.*;
import java.util.Optional;

/**
 * Created by AlexandruD on 03-Jun-17.
 */
public class DropboxManagerRemote implements RemoteFileManager {

    private DbxClientV2 client;
    private final String KEY, SECRET, ACCESS_TOKEN;

    public DropboxManagerRemote(String key, String secret, String access_token) {
        this.KEY = key;
        this.SECRET = secret;
        this.ACCESS_TOKEN = access_token;
        client = new DbxClientV2(DbxRequestConfig.newBuilder(
                "dropbox/Conference-Management").build(), ACCESS_TOKEN);
    }

    @Override
    public Optional<String> uploadFile(String fileName, byte[] fileData) {
        try {
            UploadUploader uploader = client.files().upload(fileName);
            uploader.getOutputStream().write(fileData);
            uploader.getOutputStream().flush();
            FileMetadata data = uploader.finish();
            return Optional.of(data.getName());
        }catch(DbxException e) {
            e.printStackTrace();
        }catch(IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }


    @Override
    public Optional<byte[]> getFileData(String fileName) {
        try {
            DbxDownloader<FileMetadata> downloader =  client.files().download(fileName);
            ByteArrayOutputStream res = new ByteArrayOutputStream();
            downloader.download(res);
            return Optional.of(res.toByteArray());
        }catch(DbxException e) {
            e.printStackTrace();
        }catch(IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
