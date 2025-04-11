package com.example.BazaDanychHibernate.service.Errors;

import org.springframework.http.HttpStatus;

public class NoFoundDataException extends RuntimeException {

    private String msg;
    private int status;


    public  NoFoundDataException(String msg) {
        super(msg);
        this.status = HttpStatus.NOT_FOUND.value();
    }

    public int getStatus() {
        return status;
    }
}
