package com.example.biblioteka.exception;

public class UserAlreadyExistsExeption extends RuntimeException {
    public UserAlreadyExistsExeption(String message) {
        super(message);
    }
}
