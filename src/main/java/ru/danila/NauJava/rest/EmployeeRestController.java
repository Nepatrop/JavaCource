package ru.danila.NauJava.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.danila.NauJava.dao.EmployeeRepository;
import ru.danila.NauJava.entity.Employee;

import java.util.List;

/**
 * REST контроллер для CRUD операций с сотрудниками
 */
@RestController
@RequestMapping("/api/employees")
public class EmployeeRestController {

    private final EmployeeRepository m_employeeRepository;

    @Autowired
    public EmployeeRestController(EmployeeRepository t_employeeRepository) {
        this.m_employeeRepository = t_employeeRepository;
    }

    /**
     * GET: Получить всех сотрудников
     */
    @GetMapping
    public List<Employee> getAllEmployees() {
        return m_employeeRepository.findAll();
    }

    /**
     * GET: Получить сотрудника по ID
     */
    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable Long t_id) {
        return m_employeeRepository.read(t_id);
    }

    /**
     * POST: Создать нового сотрудника
     */
    @PostMapping
    public String createEmployee(@RequestBody Employee t_employee) {
        m_employeeRepository.create(t_employee);
        return "Сотрудник создан: " + t_employee.getFirstName() + " " + t_employee.getLastName();
    }

    /**
     * PUT: Обновить сотрудника
     */
    @PutMapping("/{id}")
    public String updateEmployee(@PathVariable Long t_id, @RequestBody Employee t_employee) {
        Employee existing = m_employeeRepository.read(t_id);
        if (existing != null) {
            t_employee.setId(t_id);
            m_employeeRepository.update(t_employee);
            return "Сотрудник обновлен: " + t_employee.getFirstName() + " " + t_employee.getLastName();
        }
        return "Сотрудник с ID " + t_id + " не найден";
    }

    /**
     * DELETE: Удалить сотрудника
     */
    @DeleteMapping("/{id}")
    public String deleteEmployee(@PathVariable Long t_id) {
        Employee existing = m_employeeRepository.read(t_id);
        if (existing != null) {
            m_employeeRepository.delete(t_id);
            return "Сотрудник удален: " + existing.getFirstName() + " " + existing.getLastName();
        }
        return "Сотрудник с ID " + t_id + " не найден";
    }
}