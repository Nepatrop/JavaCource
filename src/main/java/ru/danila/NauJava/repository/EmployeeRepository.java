package ru.danila.NauJava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.danila.NauJava.entity.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // 1. Query Methods - поиск по имени и фамилии (используем And)
    List<Employee> findByFirstNameAndLastName(String t_firstName, String t_lastName);

    // 2. Query Methods - поиск по email
    Optional<Employee> findByEmail(String t_email);

    // 3. Query Methods - поиск сотрудников с зарплатой в диапазоне (используем Between)
    List<Employee> findBySalaryBetween(Double t_minSalary, Double t_maxSalary);

    // 4. Query Methods - поиск сотрудников по отделу (через ID отдела)
    @Query("SELECT e FROM Employee e WHERE e.m_department.id = :departmentId")
    List<Employee> findByDepartmentId(@Param("departmentId") Long t_departmentId);

    // 5. @Query - сложный запрос с JOIN через JPQL
    @Query("SELECT e FROM Employee e WHERE e.m_department.m_name = :departmentName AND e.m_salary > :minSalary")
    List<Employee> findEmployeesInDepartmentWithMinSalary(@Param("departmentName") String t_departmentName,
                                                          @Param("minSalary") Double t_minSalary);

    // 6. @Query - поиск сотрудников по названию должности (через связанную сущность)
    @Query("SELECT e FROM Employee e WHERE e.m_position.m_title = :positionTitle")
    List<Employee> findByPositionTitle(@Param("positionTitle") String t_positionTitle);
}
