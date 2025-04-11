package com.example.BazaDanychHibernate.service.Errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Random;

public class ErrorMessage  {
    private String message;
    private  int status;

    public ErrorMessage(String message,int status) {
        this.message = message;
        this.status = status;
    }


    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
}
