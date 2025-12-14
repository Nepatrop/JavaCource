package ru.danila.NauJava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.danila.NauJava.entity.Department;
import ru.danila.NauJava.entity.Employee;

import java.util.List;
import java.util.Optional;

/**
 * JPA репозиторий для работы с сущностью Employee
 * Обеспечивает CRUD операции и поиск по различным критериям
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
    /**
     * Найти сотрудника по электронной почте
     * @param t_email электронная почта
     * @return Optional с сотрудником
     */
    @Query("SELECT e FROM Employee e WHERE UPPER(e.m_email) = UPPER(:email)")
    Optional<Employee> findByM_emailIgnoreCase(@Param("email") String t_email);

    /**
     * Найти всех сотрудников по отделу (исключая удаленных)
     * @param t_department отдел
     * @return список сотрудников
     */
    @Query("SELECT e FROM Employee e WHERE e.m_department = :dept AND e.m_isDeleted = false")
    List<Employee> findByM_department(@Param("dept") Department t_department);

    /**
     * Найти сотрудников по имени (регистронезависимый поиск)
     * @param t_firstName имя
     * @return список сотрудников
     */
    @Query("SELECT e FROM Employee e WHERE UPPER(e.m_firstName) LIKE UPPER(CONCAT('%', :firstName, '%')) AND e.m_isDeleted = false")
    List<Employee> findByFirstNameContaining(@Param("firstName") String t_firstName);

    /**
     * Найти сотрудников по фамилии
     * @param t_lastName фамилия
     * @return список сотрудников
     */
    @Query("SELECT e FROM Employee e WHERE UPPER(e.m_lastName) LIKE UPPER(CONCAT('%', :lastName, '%')) AND e.m_isDeleted = false")
    List<Employee> findByLastNameContaining(@Param("lastName") String t_lastName);

    /**
     * Найти всех активных сотрудников
     * @return список сотрудников
     */
    @Query("SELECT e FROM Employee e WHERE e.m_isDeleted = false")
    List<Employee> findAllActive();

    /**
     * Проверить существование сотрудника по email
     * @param t_email email
     * @return true если существует
     */
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Employee e WHERE UPPER(e.m_email) = UPPER(:email)")
    boolean existsByM_emailIgnoreCase(@Param("email") String t_email);

    /**
     * Получить количество сотрудников в отделе
     * @param t_department отдел
     * @return количество
     */
    @Query("SELECT COUNT(e) FROM Employee e WHERE e.m_department = :dept AND e.m_isDeleted = false")
    long countByM_department(@Param("dept") Department t_department);
}
