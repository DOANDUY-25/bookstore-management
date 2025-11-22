package com.bookshop.bookshop.exception;

/**
 * Base exception class for all bookshop-related exceptions
 */
public class BookshopException extends RuntimeException {
    
    public BookshopException(String message) {
        super(message);
    }
    
    public BookshopException(String message, Throwable cause) {
        super(message, cause);
    }
}
