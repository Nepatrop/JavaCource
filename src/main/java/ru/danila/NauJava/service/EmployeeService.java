package ru.danila.NauJava.service;

import ru.danila.NauJava.entity.Employee;

import java.util.List;

public interface EmployeeService {
    Employee createEmployee(Employee t_employee);
    Employee getEmployeeById(Long t_id);
    List<Employee> getAllEmployees();
    Employee updateEmployee(Employee t_employee);
    void deleteEmployee(Long t_id);
    List<Employee> findByPositionTitle(String t_positionTitle);
}
