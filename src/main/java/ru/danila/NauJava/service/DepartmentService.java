package ru.danila.NauJava.service;

import org.springframework.transaction.annotation.Transactional;
import ru.danila.NauJava.entity.Department;

import java.util.List;

public interface DepartmentService {

    List<Department> getAllDepartments();

    Department createDepartment(Department t_department);

    // Транзакционный метод - удаление отдела вместе с перемещением сотрудников
    @Transactional
    void deleteDepartmentWithEmployeeTransfer(Long t_departmentId, Long t_targetDepartmentId);
}
