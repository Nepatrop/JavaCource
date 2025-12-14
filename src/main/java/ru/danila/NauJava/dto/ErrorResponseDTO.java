package ru.danila.NauJava.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для ответа при ошибке
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDTO {
    
    /**
     * HTTP статус код
     */
    private int m_status = 0;

    /**
     * Сообщение об ошибке
     */
    private String m_message = "";

    /**
     * Дополнительные детали
     */
    private String m_details = "";

    /**
     * Конструктор с статусом и сообщением
     */
    public ErrorResponseDTO(int t_status, String t_message) {
        this.m_status = t_status;
        this.m_message = t_message;
    }
}
