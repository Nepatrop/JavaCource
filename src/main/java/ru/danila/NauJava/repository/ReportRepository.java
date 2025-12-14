package ru.danila.NauJava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.danila.NauJava.entity.Report;
import ru.danila.NauJava.entity.ReportStatus;
import ru.danila.NauJava.entity.User;

import java.time.LocalDateTime;
import java.util.List;

/**
 * JPA репозиторий для работы с сущностью Report
 * Обеспечивает CRUD операции и поиск отчетов
 */
@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    
    /**
     * Найти отчеты по статусу
     * @param t_status статус
     * @return список отчетов
     */
    @Query("SELECT r FROM Report r WHERE r.m_status = :status")
    List<Report> findByM_status(@Param("status") ReportStatus t_status);

    /**
     * Найти отчеты, созданные пользователем
     * @param t_user пользователь
     * @return список отчетов
     */
    @Query("SELECT r FROM Report r WHERE r.m_createdBy = :user")
    List<Report> findByM_createdBy(@Param("user") User t_user);

    /**
     * Найти отчеты, созданные за период
     * @param t_startDate дата начала
     * @param t_endDate дата окончания
     * @return список отчетов
     */
    @Query("SELECT r FROM Report r WHERE r.m_createdDate BETWEEN :startDate AND :endDate ORDER BY r.m_createdDate DESC")
    List<Report> findByCreatedDateBetween(@Param("startDate") LocalDateTime t_startDate, @Param("endDate") LocalDateTime t_endDate);

    /**
     * Найти отчеты пользователя за период
     * @param t_user пользователь
     * @param t_startDate дата начала
     * @param t_endDate дата окончания
     * @return список отчетов
     */
    @Query("SELECT r FROM Report r WHERE r.m_createdBy = :user AND r.m_createdDate BETWEEN :startDate AND :endDate ORDER BY r.m_createdDate DESC")
    List<Report> findUserReportsByDateRange(@Param("user") User t_user, @Param("startDate") LocalDateTime t_startDate, @Param("endDate") LocalDateTime t_endDate);

    /**
     * Получить количество отчетов по статусу
     * @param t_status статус
     * @return количество
     */
    @Query("SELECT COUNT(r) FROM Report r WHERE r.m_status = :status")
    long countByM_status(@Param("status") ReportStatus t_status);

    /**
     * Получить все отчеты с сортировкой по дате (новые сначала)
     * @return список отчетов
     */
    @Query("SELECT r FROM Report r ORDER BY r.m_createdDate DESC")
    List<Report> findAllOrderByCreatedDateDesc();
}
