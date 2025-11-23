package ru.danila.NauJava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.danila.NauJava.entity.Project;

import java.time.LocalDate;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    // Query Methods - поиск проектов по статусу
    List<Project> findByStatus(String t_status);

    // Query Methods - поиск проектов между датами
    List<Project> findByStartDateBetween(LocalDate t_startDate, LocalDate t_endDate);

    // @Query - сложный запрос для поиска активных проектов
    @Query("SELECT p FROM Project p WHERE p.status = 'IN_PROGRESS' AND p.endDate IS NULL")
    List<Project> findActiveProjects();

    // @Query - поиск проектов с количеством участников
    @Query("SELECT p, COUNT(ep) FROM Project p LEFT JOIN p.employeeProjects ep GROUP BY p")
    List<Object[]> findProjectsWithEmployeeCount();
}
