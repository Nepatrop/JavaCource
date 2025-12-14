package ru.danila.NauJava.service;

import ru.danila.NauJava.entity.Report;
import ru.danila.NauJava.entity.ReportStatus;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Интерфейс сервиса для работы с отчетами
 * Управляет созданием, генерацией и получением отчетов
 */
public interface ReportService {
    
    /**
     * Получить отчет по ID
     * @param t_id ID отчета
     * @return отчет
     */
    Report getReportById(Long t_id);

    /**
     * Создать новый отчет
     * @param t_title название отчета
     * @param t_userId ID пользователя, создающего отчет
     * @return ID созданного отчета
     */
    Long createReport(String t_title, Long t_userId);

    /**
     * Получить все отчеты
     * @return список отчетов
     */
    List<Report> getAllReports();

    /**
     * Получить отчеты по статусу
     * @param t_status статус
     * @return список отчетов
     */
    List<Report> getReportsByStatus(ReportStatus t_status);

    /**
     * Удалить отчет
     * @param t_id ID отчета
     */
    void deleteReport(Long t_id);

    /**
     * Асинхронная генерация отчета по сотрудникам
     * @param t_reportId ID отчета
     * @return CompletableFuture
     */
    CompletableFuture<Void> generateEmployeeReportAsync(Long t_reportId);

    /**
     * Асинхронная генерация отчета по отделам
     * @param t_reportId ID отчета
     * @return CompletableFuture
     */
    CompletableFuture<Void> generateDepartmentReportAsync(Long t_reportId);

    /**
     * Получить отчеты по диапазону дат
     * @param t_startDate начальная дата
     * @param t_endDate конечная дата
     * @return список отчетов
     */
    List<Report> getReportsByCreatedDate(java.time.LocalDateTime t_startDate, java.time.LocalDateTime t_endDate);

    /**
     * Получить отчеты пользователя
     * @param t_userId ID пользователя
     * @return список отчетов
     */
    List<Report> getUserReports(Long t_userId);
}
