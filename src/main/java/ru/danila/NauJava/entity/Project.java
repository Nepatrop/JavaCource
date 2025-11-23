package ru.danila.NauJava.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long m_id;

    @Column(name = "name", nullable = false)
    private String m_name;

    @Column(name = "description")
    private String m_description;

    @Column(name = "start_date")
    private LocalDate m_startDate;

    @Column(name = "end_date")
    private LocalDate m_endDate;

    @Column(name = "status")
    private String m_status; // PLANNED, IN_PROGRESS, COMPLETED

    @OneToMany(mappedBy = "m_project", cascade = CascadeType.ALL)
    private List<EmployeeProject> m_employeeProjects = new ArrayList<>();

    // Конструкторы
    public Project() {}

    public Project(String t_name, String t_description, LocalDate t_startDate, String t_status) {
        this.m_name = t_name;
        this.m_description = t_description;
        this.m_startDate = t_startDate;
        this.m_status = t_status;
    }

    // Геттеры и сеттеры
    public Long getId() { return m_id; }
    public void setId(Long t_id) { this.m_id = t_id; }

    public String getName() { return m_name; }
    public void setName(String t_name) { this.m_name = t_name; }

    public String getDescription() { return m_description; }
    public void setDescription(String t_description) { this.m_description = t_description; }

    public LocalDate getStartDate() { return m_startDate; }
    public void setStartDate(LocalDate t_startDate) { this.m_startDate = t_startDate; }

    public LocalDate getEndDate() { return m_endDate; }
    public void setEndDate(LocalDate t_endDate) { this.m_endDate = t_endDate; }

    public String getStatus() { return m_status; }
    public void setStatus(String t_status) { this.m_status = t_status; }

    public List<EmployeeProject> getEmployeeProjects() { return m_employeeProjects; }
    public void setEmployeeProjects(List<EmployeeProject> t_employeeProjects) {
        this.m_employeeProjects = t_employeeProjects;
    }
}
