package ru.danila.NauJava.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для создания/обновления отдела
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDTO {
    
    /**
     * ID отдела (для обновления)
     */
    private Long m_id;

    /**
     * Название отдела
     */
    @NotBlank(message = "Название отдела не может быть пустым")
    private String m_name = "";

    /**
     * Описание отдела
     */
    private String m_description = "";

    /**
     * ID руководителя отдела
     */
    private Long m_headId;

    /**
     * ФИ руководителя (для отображения)
     */
    private String m_headName = "";

    /**
     * Количество сотрудников
     */
    private long m_employeeCount = 0L;

    /**
     * Получить название отдела
     */
    public String getName() {
        return m_name;
    }

    /**
     * Получить описание отдела
     */
    public String getDescription() {
        return m_description;
    }

    /**
     * Получить ID отдела
     */
    public Long getId() {
        return m_id;
    }

    /**
     * Получить ID руководителя
     */
    public Long getHeadId() {
        return m_headId;
    }

    /**
     * Получить ФИ руководителя
     */
    public String getHeadName() {
        return m_headName;
    }

    /**
     * Получить количество сотрудников
     */
    public long getEmployeeCount() {
        return m_employeeCount;
    }
}
