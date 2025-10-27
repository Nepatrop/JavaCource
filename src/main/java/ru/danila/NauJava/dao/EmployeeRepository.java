package ru.danila.NauJava.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.danila.NauJava.entity.Employee;

import java.util.List;
import java.util.Optional;

@Repository
public class EmployeeRepository implements CrudRepository<Employee, Long> {

    private final List<Employee> m_employeeContainer;

    @Autowired
    public EmployeeRepository(List<Employee> t_employeeContainer) {
        this.m_employeeContainer = t_employeeContainer;
    }

    // Методы для REST

    /**
     * Поиск сотрудников по имени
     */
    public List<Employee> findByFirstName(String t_firstName) {
        return m_employeeContainer.stream()
                .filter(employee -> employee.getFirstName().equalsIgnoreCase(t_firstName))
                .toList();
    }

    /**
     * Поиск сотрудников по отделу
     */
    public List<Employee> findByDepartment(String t_department) {
        return m_employeeContainer.stream()
                .filter(employee -> employee.getDepartment().equalsIgnoreCase(t_department))
                .toList();
    }

    @Override
    public void create(Employee t_employee) {
        m_employeeContainer.add(t_employee);
    }

    @Override
    public Employee read(Long t_id) {
        Optional<Employee> foundEmployee = m_employeeContainer.stream()
                .filter(employee -> employee.getId().equals(t_id))
                .findFirst();
        return foundEmployee.orElse(null);
    }

    @Override
    public void update(Employee t_employee) {
        Employee existingEmployee = read(t_employee.getId());
        if (existingEmployee != null) {
            existingEmployee.setFirstName(t_employee.getFirstName());
            existingEmployee.setLastName(t_employee.getLastName());
            existingEmployee.setDepartment(t_employee.getDepartment());
            existingEmployee.setPosition(t_employee.getPosition());
        }
    }

    @Override
    public void delete(Long t_id) {
        Employee employeeToRemove = read(t_id);
        if (employeeToRemove != null) {
            m_employeeContainer.remove(employeeToRemove);
        }
    }

    public List<Employee> findAll() {
        return m_employeeContainer;
    }
}