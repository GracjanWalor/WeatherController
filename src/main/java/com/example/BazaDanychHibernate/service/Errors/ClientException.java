package com.example.BazaDanychHibernate.service.Errors;

public class ClientException  extends RuntimeException {
    private String message;
    private  int status;

    public ClientException(String message,int status) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
