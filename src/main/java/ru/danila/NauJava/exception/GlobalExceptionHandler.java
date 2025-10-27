package ru.danila.NauJava.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Глобальный обработчик ошибок для REST API
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обработка всех исключений
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception t_exception) {
        return new ErrorResponse("Внутренняя ошибка сервера: " + t_exception.getMessage());
    }

    /**
     * Обработка когда ресурс не найден
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleResourceNotFound(ResourceNotFoundException t_exception) {
        return new ErrorResponse(t_exception.getMessage());
    }
}