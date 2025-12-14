package ru.danila.NauJava.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Класс сущности "Сотрудник"
 * Содержит основные данные о сотруднике предприятия
 */
@Entity
@Table(name = "employees", indexes = {
        @Index(name = "idx_emp_dept", columnList = "department_id"),
        @Index(name = "idx_emp_fname", columnList = "first_name"),
        @Index(name = "idx_emp_email", columnList = "email")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    
    /**
     * Уникальный идентификатор сотрудника
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long m_id = 0L;

    /**
     * Имя сотрудника
     */
    @Column(name = "first_name", nullable = false, length = 100)
    @NotBlank(message = "Имя не может быть пустым")
    private String m_firstName = "";

    /**
     * Фамилия сотрудника
     */
    @Column(name = "last_name", nullable = false, length = 100)
    @NotBlank(message = "Фамилия не может быть пустой")
    private String m_lastName = "";

    /**
     * Электронная почта сотрудника
     */
    @Column(name = "email", unique = true, nullable = false, length = 150)
    @Email(message = "Email должен быть валидным")
    @NotBlank(message = "Email не может быть пустым")
    private String m_email = "";

    /**
     * Номер телефона сотрудника
     */
    @Column(name = "phone", length = 20)
    private String m_phone = "";

    /**
     * Должность сотрудника
     */
    @Column(name = "position", nullable = false, length = 100)
    @NotBlank(message = "Должность не может быть пустой")
    private String m_position = "";

    /**
     * Дата приема на работу
     */
    @Column(name = "hire_date", nullable = false)
    private LocalDate m_hireDate = LocalDate.now();

    /**
     * Ссылка на отдел, в котором работает сотрудник
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department m_department;

    /**
     * Флаг удаления (мягкое удаление)
     */
    @Column(name = "is_deleted", nullable = false)
    private boolean m_isDeleted = false;

    /**
     * Получить ID сотрудника
     */
    public Long getId() {
        return m_id;
    }

    /**
     * Получить имя сотрудника
     */
    public String getFirstName() {
        return m_firstName;
    }

    /**
     * Получить фамилию сотрудника
     */
    public String getLastName() {
        return m_lastName;
    }

    /**
     * Получить email
     */
    public String getEmail() {
        return m_email;
    }

    /**
     * Получить должность
     */
    public String getPosition() {
        return m_position;
    }

    /**
     * Получить отдел
     */
    public Department getDepartment() {
        return m_department;
    }

    /**
     * Получить полное имя сотрудника
     * @return полное имя (Имя Фамилия)
     */
    public String getFullName() {
        return m_firstName + " " + m_lastName;
    }

    /**
     * Установить отдел
     */
    public void setDepartment(Department t_department) {
        this.m_department = t_department;
    }

    /**
     * Установить имя
     */
    public void setFirstName(String t_firstName) {
        this.m_firstName = t_firstName;
    }

    /**
     * Установить фамилию
     */
    public void setLastName(String t_lastName) {
        this.m_lastName = t_lastName;
    }

    /**
     * Установить email
     */
    public void setEmail(String t_email) {
        this.m_email = t_email;
    }

    /**
     * Установить должность
     */
    public void setPosition(String t_position) {
        this.m_position = t_position;
    }

    /**
     * Установить номер телефона
     */
    public void setPhone(String t_phone) {
        this.m_phone = t_phone;
    }

    /**
     * Установить ID сотрудника
     */
    public void setId(Long t_id) {
        this.m_id = t_id;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + m_id +
                ", firstName='" + m_firstName + '\'' +
                ", lastName='" + m_lastName + '\'' +
                ", email='" + m_email + '\'' +
                ", phone='" + m_phone + '\'' +
                ", position='" + m_position + '\'' +
                ", hireDate=" + m_hireDate +
                ", isDeleted=" + m_isDeleted +
                '}';
    }
}