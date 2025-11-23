package ru.danila.NauJava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.danila.NauJava.entity.Department;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Optional<Department> findByName(String t_name);

    // Query Methods - поиск отделов по описанию (используем Containing)
    List<Department> findByDescriptionContaining(String t_keyword);
}
