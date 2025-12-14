package ru.danila.NauJava.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для отчета о статистике
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentStatsDTO {
    
    /**
     * ID отдела
     */
    private Long m_departmentId = 0L;

    /**
     * Название отдела
     */
    private String m_departmentName = "";

    /**
     * Количество сотрудников
     */
    private long m_employeeCount = 0L;

    /**
     * Руководитель
     */
    private String m_headName = "";
}
