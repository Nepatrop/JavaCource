package ru.danila.NauJava.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long m_id;

    @Column(name = "name", nullable = false, unique = true)
    private String m_name;

    @Column(name = "description")
    private String m_description;

    @OneToMany(mappedBy = "m_department", cascade = CascadeType.ALL)
    private List<Employee> m_employees = new ArrayList<>();

    // Конструкторы
    public Department() {}

    public Department(String t_name, String t_description) {
        this.m_name = t_name;
        this.m_description = t_description;
    }

    // Геттеры и сеттеры
    public Long getId() { return m_id; }
    public void setId(Long t_id) { this.m_id = t_id; }

    public String getName() { return m_name; }
    public void setName(String t_name) { this.m_name = t_name; }

    public String getDescription() { return m_description; }
    public void setDescription(String t_description) { this.m_description = t_description; }

    public List<Employee> getEmployees() { return m_employees; }
    public void setEmployees(List<Employee> t_employees) { this.m_employees = t_employees; }
}
