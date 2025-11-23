package ru.danila.NauJava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.danila.NauJava.entity.Department;
import ru.danila.NauJava.entity.Employee;
import ru.danila.NauJava.repository.DepartmentRepository;
import ru.danila.NauJava.repository.EmployeeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository m_departmentRepository;
    private final EmployeeRepository m_employeeRepository;

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository t_departmentRepository,
                                 EmployeeRepository t_employeeRepository) {
        this.m_departmentRepository = t_departmentRepository;
        this.m_employeeRepository = t_employeeRepository;
    }

    @Override
    public List<Department> getAllDepartments() {
        return m_departmentRepository.findAll();
    }

    @Override
    public Department createDepartment(Department t_department) {
        return m_departmentRepository.save(t_department);
    }

    @Override
    @Transactional
    public void deleteDepartmentWithEmployeeTransfer(Long t_departmentId, Long t_targetDepartmentId) {
        // Находим удаляемый отдел
        Optional<Department> departmentToDelete = m_departmentRepository.findById(t_departmentId);
        if (departmentToDelete.isEmpty()) {
            throw new RuntimeException("Department not found with id: " + t_departmentId);
        }

        // Находим целевой отдел для перемещения
        Optional<Department> targetDepartment = m_departmentRepository.findById(t_targetDepartmentId);
        if (targetDepartment.isEmpty()) {
            throw new RuntimeException("Target department not found with id: " + t_targetDepartmentId);
        }

        // Находим всех сотрудников удаляемого отдела через новый метод
        List<Employee> employeesInDepartment = m_employeeRepository.findByDepartmentId(t_departmentId);

        // Перемещаем сотрудников в целевой отдел
        for (Employee employee : employeesInDepartment) {
            employee.setDepartment(targetDepartment.get());
            m_employeeRepository.save(employee); // Сохраняем изменения
        }

        // Удаляем отдел (все сотрудники уже перемещены)
        m_departmentRepository.delete(departmentToDelete.get());
    }
}
