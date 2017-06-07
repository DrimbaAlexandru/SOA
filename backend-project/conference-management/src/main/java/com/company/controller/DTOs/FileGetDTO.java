package com.company.controller.DTOs;

import sun.misc.BASE64Encoder;

/**
 * Created by Alex on 07.06.2017.
 */
public class FileGetDTO {
    private String data;

    public FileGetDTO(byte[] data)
    {
        BASE64Encoder encoder=new BASE64Encoder();
        this.data=encoder.encode(data);
    }

    public void setData(byte[] data)
    {
        BASE64Encoder encoder=new BASE64Encoder();
        this.data=encoder.encode(data);
    }
    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }
}
