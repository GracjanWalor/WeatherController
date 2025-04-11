package com.example.BazaDanychHibernate.controller;

import com.example.BazaDanychHibernate.service.Errors.ClientException;
import com.example.BazaDanychHibernate.service.Errors.ErrorMessage;
import com.example.BazaDanychHibernate.service.Errors.NoFoundDataException;
import com.example.BazaDanychHibernate.service.Errors.ServerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(ClientException.class)
    public ResponseEntity<ErrorMessage> handlerGeneralException(ClientException clientException) {
        ErrorMessage errorMessage = new ErrorMessage(clientException.getMessage(), clientException.getStatus());
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ServerException.class)
    public ResponseEntity<ErrorMessage> handlerGeneralException(ServerException serverException) {
        ErrorMessage errorMessage = new ErrorMessage(serverException.getMessage(), serverException.getStatus());
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handlerGeneralException(Exception ex) {
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), 500);
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorMessage> handlerGeneralException(NoFoundDataException noFoundDataException) {
        ErrorMessage errorMessage = new ErrorMessage(noFoundDataException.getMessage(),noFoundDataException.getStatus());
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }
}
