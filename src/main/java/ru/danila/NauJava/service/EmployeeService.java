package ru.danila.NauJava.service;

import ru.danila.NauJava.entity.Employee;

import java.util.List;

/**
 * Интерфейс бизнес-логики приложения
 * Определяет методы для работы с сотрудниками
 */
public interface EmployeeService {
    // Методы бизнес-логики
    void            hireEmployee(Long t_id, String t_firstName, String t_lastName, String t_department, String t_position);
    Employee        findEmployeeById(Long t_id);
    void            updateEmployeePosition(Long t_id, String t_newPosition);
    void            transferEmployeeDepartment(Long t_id, String t_newDepartment);
    void            deleteEmployee(Long t_id);
    List<Employee>  getAllEmployees();
}