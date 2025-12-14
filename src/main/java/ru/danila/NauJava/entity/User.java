package ru.danila.NauJava.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Класс сущности "Пользователь системы"
 * Содержит данные пользователя приложения с ролевым доступом
 */
@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_user_username", columnList = "username"),
        @Index(name = "idx_user_email", columnList = "email")
})
@Data
@NoArgsConstructor
@AllArgsConstructor

public class User {
    
    /**
     * Уникальный идентификатор пользователя
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long m_id = 0L;

    /**
     * Имя пользователя (login)
     */
    @Column(name = "username", unique = true, nullable = false, length = 50)
    @NotBlank(message = "Имя пользователя не может быть пустым")
    private String m_username = "";

    /**
     * Пароль пользователя (в зашифрованном виде)
     */
    @Column(name = "password", nullable = false)
    @NotBlank(message = "Пароль не может быть пустым")
    private String m_password = "";

    /**
     * Электронная почта пользователя
     */
    @Column(name = "email", unique = true, nullable = false, length = 150)
    @Email(message = "Email должен быть валидным")
    @NotBlank(message = "Email не может быть пустым")
    private String m_email = "";

    /**
     * Флаг активации аккаунта
     */
    @Column(name = "is_enabled", nullable = false)
    private boolean m_isEnabled = true;

    /**
     * Дата создания аккаунта
     */
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime m_createdDate = LocalDateTime.now();

    /**
     * Дата последнего входа
     */
    @Column(name = "last_login_date")
    private LocalDateTime m_lastLoginDate;

    /**
     * Ссылка на сотрудника (если пользователь - сотрудник)
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee m_employee;

    /**
     * Роли пользователя
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> m_roles = new HashSet<>();

    /**
     * Конструктор с основными параметрами
     */
    public User(String t_username, String t_password, String t_email) {
        this.m_username = t_username;
        this.m_password = t_password;
        this.m_email = t_email;
    }

    /**
     * Добавить роль пользователю
     */
    public void addRole(Role t_role) {
        this.m_roles.add(t_role);
    }

    /**
     * Проверить, есть ли роль у пользователя
     */
    public boolean hasRole(String t_roleName) {
        return m_roles.stream()
                .anyMatch(t_role -> t_role.getName().equals(t_roleName));
    }

    /**
     * Получить ID пользователя
     */
    public Long getId() {
        return m_id;
    }

    /**
     * Получить имя пользователя (username)
     */
    public String getUsername() {
        return m_username;
    }

    /**
     * Получить пароль
     */
    public String getPassword() {
        return m_password;
    }

    /**
     * Получить email
     */
    public String getEmail() {
        return m_email;
    }

    /**
     * Получить роли пользователя
     */
    public Set<Role> getRoles() {
        return m_roles;
    }

    /**
     * Проверить, активен ли пользователь
     */
    public boolean isEnabled() {
        return m_isEnabled;
    }

    /**
     * Установить имя пользователя
     */
    public void setUsername(String t_username) {
        this.m_username = t_username;
    }

    /**
     * Установить пароль
     */
    public void setPassword(String t_password) {
        this.m_password = t_password;
    }

    /**
     * Установить email
     */
    public void setEmail(String t_email) {
        this.m_email = t_email;
    }

    /**
     * Установить статус активации пользователя
     */
    public void setEnabled(boolean t_enabled) {
        this.m_isEnabled = t_enabled;
    }

    /**
     * Установить роли
     */
    public void setRoles(Set<Role> t_roles) {
        this.m_roles = t_roles;
    }

    /**
     * Получить имя пользователя (alias)
     * @deprecated используйте getUsername()
     */
    @Deprecated
    public String getFirstName() {
        return m_username;
    }

    /**
     * Установить имя пользователя (alias)
     * @deprecated используйте setUsername()
     */
    @Deprecated
    public void setFirstName(String t_firstName) {
        this.m_username = t_firstName;
    }

    /**
     * Получить фамилию (не используется для User)
     */
    public String getLastName() {
        return "";
    }

    /**
     * Установить фамилию (не используется для User)
     */
    @Deprecated
    public void setLastName(String t_lastName) {
        // не используется
    }

    /**
     * Установить ID пользователя
     */
    public void setId(Long t_id) {
        this.m_id = t_id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + m_id +
                ", username='" + m_username + '\'' +
                ", email='" + m_email + '\'' +
                ", isEnabled=" + m_isEnabled +
                ", createdDate=" + m_createdDate +
                ", roles=" + m_roles +
                '}';
    }
}
