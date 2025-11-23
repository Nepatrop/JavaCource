package ru.danila.NauJava.dao;

import ru.danila.NauJava.entity.Employee;

import java.util.List;

/**
 * Кастомный интерфейс для Criteria API запросов
 */
public interface EmployeeRepositoryCustom {

    // 1. Аналог метода: findByPositionTitle (из п.5)
    List<Employee> findEmployeesByPositionTitle(String t_positionTitle);

    // 2. Дополнительный метод - поиск сотрудников с зарплатой выше указанной
    List<Employee> findEmployeesWithSalaryAbove(Double t_minSalary);
}
