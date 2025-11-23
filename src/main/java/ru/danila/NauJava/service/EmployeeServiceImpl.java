package ru.danila.NauJava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.danila.NauJava.dao.EmployeeRepository;
import ru.danila.NauJava.entity.Employee;

import java.util.List;

/**
 * Реализация бизнес-логики приложения
 * Содержит основную логику работы с сотрудниками
 */
@Service // Помечаем как сервисный компонент
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository m_employeeRepository;

    // Внедрение зависимости через конструктор
    @Autowired
    public EmployeeServiceImpl(EmployeeRepository t_employeeRepository) {
        this.m_employeeRepository = t_employeeRepository;
    }

    @Override
    public void hireEmployee(Long t_id, String t_firstName, String t_lastName, String t_department, String t_position) {
        // Проверяем, что сотрудник с таким ID еще не существует
        if (findEmployeeById(t_id) != null) {
            System.out.println("Ошибка: сотрудник с ID " + t_id + " уже существует!");
            return;
        }

        Employee newEmployee = new Employee();
        newEmployee.setId(t_id);
        newEmployee.setFirstName(t_firstName);
        newEmployee.setLastName(t_lastName);
        newEmployee.setDepartment(t_department);
        newEmployee.setPosition(t_position);
        m_employeeRepository.create(newEmployee);
    }

    @Override
    public Employee findEmployeeById(Long t_id) {
        // Просто делегируем вызов репозиторию
        return m_employeeRepository.read(t_id);
    }

    @Override
    public void updateEmployeePosition(Long t_id, String t_newPosition) {
        // Находим сотрудника и обновляем его должность
        Employee employee = m_employeeRepository.read(t_id);
        if (employee != null) {
            employee.setPosition(t_newPosition);
            m_employeeRepository.update(employee);
        }
    }

    @Override
    public void transferEmployeeDepartment(Long t_id, String t_newDepartment) {
        // Находим сотрудника и переводим в другой отдел
        Employee employee = m_employeeRepository.read(t_id);
        if (employee != null) {
            employee.setDepartment(t_newDepartment);
            m_employeeRepository.update(employee);
        }
    }

    @Override
    public void deleteEmployee(Long t_id) {
        // Удаляем сотрудника
        m_employeeRepository.delete(t_id);
    }

    @Override
    public List<Employee> getAllEmployees() {
        // Получаем список всех сотрудников
        return m_employeeRepository.findAll();
    }
}
