package com.bookshop.bookshop.exception;

/**
 * Exception thrown when user is not authorized to perform an action
 */
public class UnauthorizedException extends BookshopException {
    
    public UnauthorizedException(String message) {
        super(message);
    }
    
    public UnauthorizedException() {
        super("You are not authorized to perform this action");
    }
}
