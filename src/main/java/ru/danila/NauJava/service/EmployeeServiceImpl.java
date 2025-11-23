package ru.danila.NauJava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.danila.NauJava.entity.Employee;
import ru.danila.NauJava.repository.EmployeeRepository;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository m_employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository t_employeeRepository) {
        this.m_employeeRepository = t_employeeRepository;
    }

    @Override
    public Employee createEmployee(Employee t_employee) {
        return m_employeeRepository.save(t_employee);
    }

    @Override
    public Employee getEmployeeById(Long t_id) {
        return m_employeeRepository.findById(t_id).orElse(null);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return m_employeeRepository.findAll();
    }

    @Override
    public Employee updateEmployee(Employee t_employee) {
        if (m_employeeRepository.existsById(t_employee.getId())) {
            return m_employeeRepository.save(t_employee);
        }
        return null;
    }

    @Override
    public void deleteEmployee(Long t_id) {
        m_employeeRepository.deleteById(t_id);
    }

    @Override
    public List<Employee> findByPositionTitle(String t_positionTitle) {
        return m_employeeRepository.findByPositionTitle(t_positionTitle);
    }
}
