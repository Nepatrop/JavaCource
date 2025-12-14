package ru.danila.NauJava.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для создания/обновления сотрудника
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
    
    /**
     * ID сотрудника (для обновления)
     */
    private Long m_id;

    /**
     * Имя сотрудника
     */
    @NotBlank(message = "Имя не может быть пустым")
    private String m_firstName = "";

    /**
     * Фамилия сотрудника
     */
    @NotBlank(message = "Фамилия не может быть пустой")
    private String m_lastName = "";

    /**
     * Электронная почта
     */
    @Email(message = "Email должен быть валидным")
    @NotBlank(message = "Email не может быть пустым")
    private String m_email = "";

    /**
     * Номер телефона
     */
    private String m_phone = "";

    /**
     * Должность
     */
    @NotBlank(message = "Должность не может быть пустой")
    private String m_position = "";

    /**
     * ID отдела
     */
    private Long m_departmentId;

    /**
     * Название отдела (для отображения)
     */
    private String m_departmentName = "";

    /**
     * Флаг администратора
     */
    private boolean m_isAdmin = false;

    /**
     * Получить ID сотрудника
     */
    public Long getId() {
        return m_id;
    }

    /**
     * Получить имя (совместимость)
     */
    public String getFirstName() {
        return m_firstName;
    }

    /**
     * Получить фамилию (совместимость)
     */
    public String getLastName() {
        return m_lastName;
    }

    /**
     * Получить email (совместимость)
     */
    public String getEmail() {
        return m_email;
    }

    /**
     * Получить телефон
     */
    public String getPhone() {
        return m_phone;
    }

    /**
     * Получить должность
     */
    public String getPosition() {
        return m_position;
    }

    /**
     * Получить ID отдела
     */
    public Long getDepartmentId() {
        return m_departmentId;
    }

    /**
     * Получить название отдела
     */
    public String getDepartmentName() {
        return m_departmentName;
    }

    /**
     * Установить ID
     */
    public void setM_id(Long t_id) {
        this.m_id = t_id;
    }

    /**
     * Установить имя
     */
    public void setM_firstName(String t_firstName) {
        this.m_firstName = t_firstName;
    }

    /**
     * Установить фамилию
     */
    public void setM_lastName(String t_lastName) {
        this.m_lastName = t_lastName;
    }

    /**
     * Установить email
     */
    public void setM_email(String t_email) {
        this.m_email = t_email;
    }

    /**
     * Установить телефон
     */
    public void setM_phone(String t_phone) {
        this.m_phone = t_phone;
    }

    /**
     * Установить должность
     */
    public void setM_position(String t_position) {
        this.m_position = t_position;
    }

    /**
     * Установить ID отдела
     */
    public void setM_departmentId(Long t_departmentId) {
        this.m_departmentId = t_departmentId;
    }

    /**
     * Установить название отдела
     */
    public void setM_departmentName(String t_departmentName) {
        this.m_departmentName = t_departmentName;
    }

    /**
     * Получить флаг администратора
     */
    public boolean isAdmin() {
        return m_isAdmin;
    }

    /**
     * Установить флаг администратора
     */
    public void setM_isAdmin(boolean t_isAdmin) {
        this.m_isAdmin = t_isAdmin;
    }
}
