package com.project.ecom.java_ecom_cart_service.controllers;

import com.project.ecom.java_ecom_cart_service.exceptions.ExternalServiceException;
import com.project.ecom.java_ecom_cart_service.exceptions.NoActiveCartException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NoActiveCartException.class)
    public ResponseEntity<String> handleNoActiveCartException(NoActiveCartException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage());
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<String> handleExternalServiceException(ExternalServiceException e) {
        return ResponseEntity
                .status(e.getStatusCode())
                .body(e.getErrorBody());
    }
}
