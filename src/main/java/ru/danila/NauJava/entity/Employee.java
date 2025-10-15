package ru.danila.NauJava.entity;

/**
 * Класс сущности "Сотрудник"
 * Содержит основные данные о сотруднике
 */
public class Employee {
    // Поля класса с префиксом m_ и инициализацией по умолчанию
    private Long    m_id = 0L;
    private String  m_firstName = "";
    private String  m_lastName = "";
    private String  m_department = "";
    private String  m_position = "";

    // Геттеры и сеттеры для доступа к приватным полям
    public Long getId() {
        return m_id;
    }

    public void setId(Long t_id) {
        this.m_id = t_id;
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

    public String getDepartment() {
        return m_department;
    }

    public void setDepartment(String t_department) {
        this.m_department = t_department;
    }

    public String getPosition() {
        return m_position;
    }

    public void setPosition(String t_position) {
        this.m_position = t_position;
    }

    // toString для вывода информации о сотруднике
    @Override
    public String toString() {
        return "Employee{" +
                "id=" + m_id +
                ", firstName='" + m_firstName + '\'' +
                ", lastName='" + m_lastName + '\'' +
                ", department='" + m_department + '\'' +
                ", position='" + m_position + '\'' +
                '}';
    }
}