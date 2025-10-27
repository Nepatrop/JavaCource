package ru.danila.NauJava.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String t_message) {
        super(t_message);
    }
}