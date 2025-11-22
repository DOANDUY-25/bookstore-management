package com.bookshop.bookshop.exception;

/**
 * Exception thrown when payment processing fails
 */
public class InvalidPaymentException extends BookshopException {
    
    public InvalidPaymentException(String message) {
        super(message);
    }
    
    public InvalidPaymentException(String message, Throwable cause) {
        super(message, cause);
    }
}
