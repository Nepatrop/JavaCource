package ru.danila.NauJava.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "positions")
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long m_id;

    @Column(name = "title", nullable = false, unique = true)
    private String m_title;

    @Column(name = "description")
    private String m_description;

    @Column(name = "salary_grade")
    private String m_salaryGrade;

    @OneToMany(mappedBy = "m_position", cascade = CascadeType.ALL)
    private List<Employee> m_employees = new ArrayList<>();

    // Конструкторы
    public Position() {}

    public Position(String t_title, String t_description, String t_salaryGrade) {
        this.m_title = t_title;
        this.m_description = t_description;
        this.m_salaryGrade = t_salaryGrade;
    }

    // Геттеры и сеттеры
    public Long getId() { return m_id; }
    public void setId(Long t_id) { this.m_id = t_id; }

    public String getTitle() { return m_title; }
    public void setTitle(String t_title) { this.m_title = t_title; }

    public String getDescription() { return m_description; }
    public void setDescription(String t_description) { this.m_description = t_description; }

    public String getSalaryGrade() { return m_salaryGrade; }
    public void setSalaryGrade(String t_salaryGrade) { this.m_salaryGrade = t_salaryGrade; }

    public List<Employee> getEmployees() { return m_employees; }
    public void setEmployees(List<Employee> t_employees) { this.m_employees = t_employees; }
}
