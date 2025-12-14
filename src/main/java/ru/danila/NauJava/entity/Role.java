package ru.danila.NauJava.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс сущности "Роль"
 * Содержит информацию о ролях в системе (ADMIN, USER)
 */
@Entity
@Table(name = "roles", indexes = {
        @Index(name = "idx_role_name", columnList = "name")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    
    /**
     * Уникальный идентификатор роли
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long m_id = 0L;

    /**
     * Название роли (ROLE_ADMIN, ROLE_USER)
     */
    @Column(name = "name", unique = true, nullable = false, length = 50)
    @NotBlank(message = "Название роли не может быть пустым")
    private String m_name = "";

    /**
     * Описание роли
     */
    @Column(name = "description", length = 200)
    private String m_description = "";

    /**
     * Конструктор с названием роли
     */
    public Role(String t_name) {
        this.m_name = t_name;
    }

    /**
     * Конструктор с названием и описанием
     */
    public Role(String t_name, String t_description) {
        this.m_name = t_name;
        this.m_description = t_description;
    }

    /**
     * Получить название роли
     */
    public String getName() {
        return m_name;
    }

    /**
     * Получить описание роли
     */
    public String getDescription() {
        return m_description;
    }

    /**
     * Получить ID роли
     */
    public Long getId() {
        return m_id;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + m_id +
                ", name='" + m_name + '\'' +
                ", description='" + m_description + '\'' +
                '}';
    }
}
