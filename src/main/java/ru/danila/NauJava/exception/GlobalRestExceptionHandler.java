package ru.danila.NauJava.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import ru.danila.NauJava.dto.ErrorResponseDTO;

import java.util.HashMap;
import java.util.Map;

/**
 * Глобальный обработчик исключений для REST API
 * Перехватывает все исключения и возвращает стандартизированный ответ
 */
@Slf4j
@ControllerAdvice
public class GlobalRestExceptionHandler {

    /**
     * Обработка ResourceNotFoundException
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(
            ResourceNotFoundException t_exception,
            WebRequest t_request) {
        log.warn("Ресурс не найден: {}", t_exception.getMessage());
        
        ErrorResponseDTO s_error = new ErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                t_exception.getMessage(),
                t_request.getDescription(false)
        );
        return new ResponseEntity<>(s_error, HttpStatus.NOT_FOUND);
    }

    /**
     * Обработка ошибок валидации
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponseDTO> handleValidationException(
            MethodArgumentNotValidException t_exception,
            WebRequest t_request) {
        log.warn("Ошибка валидации: {}", t_exception.getMessage());
        
        Map<String, String> s_errors = new HashMap<>();
        t_exception.getBindingResult().getAllErrors().forEach((t_error) -> {
            String s_fieldName = ((FieldError) t_error).getField();
            String s_errorMessage = t_error.getDefaultMessage();
            s_errors.put(s_fieldName, s_errorMessage);
        });
        
        ErrorResponseDTO s_error = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Ошибка валидации данных",
                s_errors.toString()
        );
        return new ResponseEntity<>(s_error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обработка IllegalArgumentException
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponseDTO> handleIllegalArgumentException(
            IllegalArgumentException t_exception,
            WebRequest t_request) {
        log.warn("Неверный аргумент: {}", t_exception.getMessage());
        
        ErrorResponseDTO s_error = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                t_exception.getMessage(),
                t_request.getDescription(false)
        );
        return new ResponseEntity<>(s_error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обработка всех остальных исключений
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponseDTO> handleGlobalException(
            Exception t_exception,
            WebRequest t_request) {
        log.error("Внутренняя ошибка сервера: ", t_exception);
        
        ErrorResponseDTO s_error = new ErrorResponseDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Внутренняя ошибка сервера",
                t_exception.getMessage()
        );
        return new ResponseEntity<>(s_error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
