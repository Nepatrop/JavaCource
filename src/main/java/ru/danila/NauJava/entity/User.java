package ru.danila.NauJava.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Сущность пользователя приложения
 */
public class User {
    private Long m_id = 0L;
    private String m_username = "";
    private String m_password = "";
    private String m_firstName = "";
    private String m_lastName = "";
    private List<String> m_roles = new ArrayList<>();

    // Конструктор по умолчанию
    public User() {
    }

    // Конструктор с параметрами
    public User(String t_username, String t_password, String t_role) {
        this.m_username = t_username;
        this.m_password = t_password;
        this.m_roles.add(t_role);
    }

    // Геттеры и сеттеры
    public Long getId() {
        return m_id;
    }

    public void setId(Long t_id) {
        this.m_id = t_id;
    }

    public String getUsername() {
        return m_username;
    }

    public void setUsername(String t_username) {
        this.m_username = t_username;
    }

    public String getPassword() {
        return m_password;
    }

    public void setPassword(String t_password) {
        this.m_password = t_password;
    }

    public String getFirstName() {
        return m_firstName;
    }

    public void setFirstName(String t_firstName) {
        this.m_firstName = t_firstName;
    }

    public String getLastName() {
        return m_lastName;
    }

    public void setLastName(String t_lastName) {
        this.m_lastName = t_lastName;
    }

    public List<String> getRoles() {
        return m_roles;
    }

    public void setRoles(List<String> t_roles) {
        this.m_roles = t_roles;
    }

    public void addRole(String t_role) {
        this.m_roles.add(t_role);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + m_id +
                ", username='" + m_username + '\'' +
                ", firstName='" + m_firstName + '\'' +
                ", lastName='" + m_lastName + '\'' +
                ", roles=" + m_roles +
                '}';
    }
}
