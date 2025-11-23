package ru.danila.NauJava.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "employee_projects")
public class EmployeeProject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long m_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee m_employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project m_project;

    @Column(name = "role_in_project")
    private String m_roleInProject;

    @Column(name = "assignment_date")
    private LocalDate m_assignmentDate;

    @Column(name = "completion_date")
    private LocalDate m_completionDate;

    // Конструкторы
    public EmployeeProject() {}

    public EmployeeProject(Employee t_employee, Project t_project, String t_roleInProject,
                           LocalDate t_assignmentDate) {
        this.m_employee = t_employee;
        this.m_project = t_project;
        this.m_roleInProject = t_roleInProject;
        this.m_assignmentDate = t_assignmentDate;
    }

    // Геттеры и сеттеры
    public Long getId() { return m_id; }
    public void setId(Long t_id) { this.m_id = t_id; }

    public Employee getEmployee() { return m_employee; }
    public void setEmployee(Employee t_employee) { this.m_employee = t_employee; }

    public Project getProject() { return m_project; }
    public void setProject(Project t_project) { this.m_project = t_project; }

    public String getRoleInProject() { return m_roleInProject; }
    public void setRoleInProject(String t_roleInProject) { this.m_roleInProject = t_roleInProject; }

    public LocalDate getAssignmentDate() { return m_assignmentDate; }
    public void setAssignmentDate(LocalDate t_assignmentDate) { this.m_assignmentDate = t_assignmentDate; }

    public LocalDate getCompletionDate() { return m_completionDate; }
    public void setCompletionDate(LocalDate t_completionDate) { this.m_completionDate = t_completionDate; }
}
