package com.example.BazaDanychHibernate.service.Errors;

public class ServerException extends  RuntimeException {
    private String message;
    private  int status;

    public ServerException(String message,int status) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
