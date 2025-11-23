package ru.danila.NauJava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.danila.NauJava.entity.Position;

import java.util.List;
import java.util.Optional;

public interface PositionRepository extends JpaRepository<Position, Long> {

    Optional<Position> findByTitle(String t_title);

    // Query Methods - поиск должностей по грейду зарплаты
    List<Position> findBySalaryGrade(String t_salaryGrade);
}
