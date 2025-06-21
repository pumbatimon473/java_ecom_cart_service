package com.project.ecom.java_ecom_cart_service.exceptions;

public class NoActiveCartException extends RuntimeException {
    public NoActiveCartException(Long customerId) {
        super(String.format("No active cart found for customer id %d", customerId));
    }
}
