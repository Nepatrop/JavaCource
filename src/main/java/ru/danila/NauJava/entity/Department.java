package ru.danila.NauJava.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Класс сущности "Отдел"
 * Содержит информацию об отделе предприятия
 */
@Entity
@Table(name = "departments", indexes = {
        @Index(name = "idx_dept_name", columnList = "name")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Department {
    
    /**
     * Уникальный идентификатор отдела
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long m_id = 0L;

    /**
     * Название отдела
     */
    @Column(name = "name", unique = true, nullable = false, length = 100)
    @NotBlank(message = "Название отдела не может быть пустым")
    private String m_name = "";

    /**
     * Описание отдела
     */
    @Column(name = "description", length = 500)
    private String m_description = "";

    /**
     * Руководитель отдела (ссылка на Employee)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "head_id")
    private Employee m_head;

    /**
     * Дата создания отдела
     */
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime m_createdDate = LocalDateTime.now();

    /**
     * Список сотрудников отдела
     */
    @OneToMany(mappedBy = "m_department", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Employee> m_employees = new HashSet<>();

    /**
     * Флаг удаления (мягкое удаление)
     */
    @Column(name = "is_deleted", nullable = false)
    private boolean m_isDeleted = false;

    /**
     * Конструктор для создания отдела с названием и описанием
     */
    public Department(String t_name, String t_description) {
        this.m_name = t_name;
        this.m_description = t_description;
        this.m_createdDate = LocalDateTime.now();
        this.m_employees = new HashSet<>();
    }

    @Override
    public String toString() {
        return "Department{" +
                "id=" + m_id +
                ", name='" + m_name + '\'' +
                ", description='" + m_description + '\'' +
                ", createdDate=" + m_createdDate +
                ", isDeleted=" + m_isDeleted +
                '}';
    }
}
