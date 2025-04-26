package com.example.shopping.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super("User " + id + " not found");
    }

    public UserNotFoundException(Long id, Throwable cause) {
        super("User " + id + " not found", cause);
    }
}
