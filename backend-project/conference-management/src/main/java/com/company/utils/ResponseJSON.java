package com.company.utils;

import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 04.06.2017.
 */
public class ResponseJSON<T> {
    private T resp;
    private List<String> warnings=new ArrayList<>();
    private List<String> errors=new ArrayList<>();

    public ResponseJSON(){}

    public ResponseJSON(T _resp){resp=_resp;}

    public void addError(String error) {
        this.errors.add(error);
    }

    public void setResp(T resp) {
        this.resp = resp;
    }

    public void addWarning(String warning) {
        warnings.add(warning);
    }

    public List<String> getErrors() {
        return errors;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public T getResp() {
        return resp;
    }
}
