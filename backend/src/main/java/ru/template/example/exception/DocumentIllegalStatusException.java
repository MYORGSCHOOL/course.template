package ru.template.example.exception;

public class DocumentIllegalStatusException extends RuntimeException {
    public DocumentIllegalStatusException(String message) {
        super(message);
    }
}
