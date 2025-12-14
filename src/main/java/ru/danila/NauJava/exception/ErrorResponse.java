package ru.danila.NauJava.exception;

public class ErrorResponse {
    private String m_message;

    public ErrorResponse(String t_message) {
        this.m_message = t_message;
    }

    public String getMessage() {
        return m_message;
    }

    public void setMessage(String t_message) {
        this.m_message = t_message;
    }
}