package ru.demyan.exception;

public class WrongUserException extends RuntimeException {
    public WrongUserException() {}

    public WrongUserException(String message) {
        super(message);
    }
}
