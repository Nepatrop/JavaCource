package ru.danila.NauJava.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.danila.NauJava.entity.Employee;

import java.util.List;
import java.util.Optional;

/**
 * Реализация репозитория для работы с сотрудниками
 * Содержит логику доступа к данным
 */
@Repository // Помечаем как компонент доступа к данным
public class EmployeeRepository implements CrudRepository<Employee, Long> {

    private final List<Employee> m_employeeContainer;

    // Внедрение зависимости через конструктор
    @Autowired
    public EmployeeRepository(List<Employee> t_employeeContainer) {
        this.m_employeeContainer = t_employeeContainer;
    }

    @Override
    public void create(Employee t_employee) {
        // Проверяем, нет ли уже сотрудника с таким ID
        if (read(t_employee.getId()) == null) {
            m_employeeContainer.add(t_employee);
        } else {
            System.out.println("Сотрудник с ID " + t_employee.getId() + " уже существует!");
        }
    }

    @Override
    public Employee read(Long t_id) {
        // Ищем сотрудника по ID с помощью Stream API
        Optional<Employee> foundEmployee = m_employeeContainer.stream()
                .filter(employee -> employee.getId().equals(t_id))
                .findFirst();
        return foundEmployee.orElse(null); // Возвращаем null если не найден
    }

    @Override
    public void update(Employee t_employee) {
        // Находим существующего сотрудника и обновляем его данные
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
        // Удаляем сотрудника из списка
        Employee employeeToRemove = read(t_id);
        if (employeeToRemove != null) {
            m_employeeContainer.remove(employeeToRemove);
        }
    }

    /**
     * Дополнительный метод для получения всех сотрудников
     */
    public List<Employee> findAll() {
        return m_employeeContainer;
    }
}