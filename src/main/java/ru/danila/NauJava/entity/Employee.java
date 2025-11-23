package ru.danila.NauJava.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long m_id;

    @Column(name = "first_name", nullable = false)
    private String m_firstName;

    @Column(name = "last_name", nullable = false)
    private String m_lastName;

    @Column(name = "email", unique = true)
    private String m_email;

    @Column(name = "phone")
    private String m_phone;

    @Column(name = "hire_date")
    private LocalDate m_hireDate;

    @Column(name = "salary")
    private Double m_salary;

    // Связи
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department m_department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id")
    private Position m_position;

    @OneToMany(mappedBy = "m_employee", cascade = CascadeType.ALL)
    private List<EmployeeProject> m_employeeProjects = new ArrayList<>();

    // Конструкторы
    public Employee() {}

    public Employee(String t_firstName, String t_lastName, String t_email,
                    LocalDate t_hireDate, Double t_salary) {
        this.m_firstName = t_firstName;
        this.m_lastName = t_lastName;
        this.m_email = t_email;
        this.m_hireDate = t_hireDate;
        this.m_salary = t_salary;
    }

    // Геттеры и сеттеры
    public Long getId() { return m_id; }
    public void setId(Long t_id) { this.m_id = t_id; }

    public String getFirstName() { return m_firstName; }
    public void setFirstName(String t_firstName) { this.m_firstName = t_firstName; }

    public String getLastName() { return m_lastName; }
    public void setLastName(String t_lastName) { this.m_lastName = t_lastName; }

    public String getEmail() { return m_email; }
    public void setEmail(String t_email) { this.m_email = t_email; }

    public String getPhone() { return m_phone; }
    public void setPhone(String t_phone) { this.m_phone = t_phone; }

    public LocalDate getHireDate() { return m_hireDate; }
    public void setHireDate(LocalDate t_hireDate) { this.m_hireDate = t_hireDate; }

    public Double getSalary() { return m_salary; }
    public void setSalary(Double t_salary) { this.m_salary = t_salary; }

    public Department getDepartment() { return m_department; }
    public void setDepartment(Department t_department) { this.m_department = t_department; }

    public Position getPosition() { return m_position; }
    public void setPosition(Position t_position) { this.m_position = t_position; }

    public List<EmployeeProject> getEmployeeProjects() { return m_employeeProjects; }
    public void setEmployeeProjects(List<EmployeeProject> t_employeeProjects) {
        this.m_employeeProjects = t_employeeProjects;
    }
}
