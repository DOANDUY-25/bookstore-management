package com.bookshop.bookshop.exception;

/**
 * Exception thrown when attempting to register a user with an existing username
 */
public class DuplicateUserException extends BookshopException {
    
    private String userName;
    
    public DuplicateUserException(String userName, boolean isUserName) {
        super(String.format("Username '%s' is already taken", userName));
        this.userName = userName;
    }
    
    public String getUserName() {
        return userName;
    }
}
