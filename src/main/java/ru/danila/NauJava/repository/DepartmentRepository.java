package ru.danila.NauJava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.danila.NauJava.entity.Department;

import java.util.List;
import java.util.Optional;

/**
 * JPA репозиторий для работы с сущностью Department
 * Обеспечивает CRUD операции и поиск отделов
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    
    /**
     * Найти отдел по названию
     * @param t_name название
     * @return Optional с отделом
     */
    @Query("SELECT d FROM Department d WHERE UPPER(d.m_name) = UPPER(:name)")
    Optional<Department> findByM_nameIgnoreCase(@Param("name") String t_name);

    /**
     * Найти все активные отделы
     * @return список отделов
     */
    @Query("SELECT d FROM Department d WHERE d.m_isDeleted = false")
    List<Department> findAllActive();

    /**
     * Проверить существование отдела по названию
     * @param t_name название
     * @return true если существует
     */
    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM Department d WHERE UPPER(d.m_name) = UPPER(:name)")
    boolean existsByM_nameIgnoreCase(@Param("name") String t_name);

    /**
     * Получить количество сотрудников в отделе
     * @param t_departmentId id отдела
     * @return количество
     */
    @Query("SELECT COUNT(e) FROM Employee e WHERE e.m_department.m_id = :deptId AND e.m_isDeleted = false")
    long countEmployeesByDepartment(@Param("deptId") Long t_departmentId);
}
