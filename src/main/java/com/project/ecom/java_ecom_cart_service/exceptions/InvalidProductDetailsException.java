package com.project.ecom.java_ecom_cart_service.exceptions;

public class InvalidProductDetailsException extends RuntimeException {
    public InvalidProductDetailsException(String errMsg) {
        super(errMsg);
    }
}
