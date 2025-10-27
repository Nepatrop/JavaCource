package ru.danila.NauJava.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.danila.NauJava.dao.EmployeeRepository;
import ru.danila.NauJava.entity.Employee;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST контроллер для операций с сотрудниками
 */
@RestController
@RequestMapping("/api/employees")
@Tag(name = "Employee Search API", description = "Поиск и статистика сотрудников")
public class EmployeeCustomController {

    private final EmployeeRepository m_employeeRepository;

    @Autowired
    public EmployeeCustomController(EmployeeRepository t_employeeRepository) {
        this.m_employeeRepository = t_employeeRepository;
    }

    @Operation(summary = "Поиск сотрудников по имени")
    @GetMapping("/search/first-name")
    public List<Employee> getEmployeesByFirstName(@RequestParam("firstName") String t_firstName) {
        return m_employeeRepository.findByFirstName(t_firstName);
    }

    @Operation(summary = "Поиск сотрудников по отделу")
    @GetMapping("/search/department")
    public List<Employee> getEmployeesByDepartment(@RequestParam("department") String t_department) {
        return m_employeeRepository.findByDepartment(t_department);
    }

    @Operation(summary = "Поиск сотрудников по должности")
    @GetMapping("/search/position")
    public List<Employee> getEmployeesByPosition(@RequestParam("position") String t_position) {
        List<Employee> allEmployees = m_employeeRepository.findAll();
        return allEmployees.stream()
                .filter(employee -> employee.getPosition().toLowerCase()
                        .contains(t_position.toLowerCase()))
                .toList();
    }

    @Operation(summary = "Статистика по отделам")
    @GetMapping("/stats/department")
    public String getDepartmentStats() {
        List<Employee> allEmployees = m_employeeRepository.findAll();

        if (allEmployees.isEmpty()) {
            return "Нет данных о сотрудниках";
        }

        var stats = allEmployees.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        Employee::getDepartment,
                        java.util.stream.Collectors.counting()
                ));

        return "Статистика по отделам: " + stats;
    }
}