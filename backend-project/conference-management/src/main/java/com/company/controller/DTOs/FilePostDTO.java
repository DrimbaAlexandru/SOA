package com.company.controller.DTOs;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Alex on 07.06.2017.
 */
public class FilePostDTO
{
    private String type;
    private MultipartFile data;

    public FilePostDTO() {

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public MultipartFile getData() {
        return data;
    }

    public void setData(MultipartFile data) {
        this.data = data;
    }
}
