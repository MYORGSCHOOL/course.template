package ru.template.example.exception;

public class NotFoundStatus extends RuntimeException {
    public NotFoundStatus(String message) {
        super(message);
    }
}
