package ru.danila.NauJava.service;

import ru.danila.NauJava.dto.DepartmentDTO;
import ru.danila.NauJava.dto.DepartmentStatsDTO;

import java.util.List;

/**
 * Интерфейс сервиса для работы с отделами
 * Определяет методы управления отделами и статистикой
 */
public interface DepartmentService {
    
    /**
     * Создать новый отдел
     * @param t_departmentDTO DTO с данными отдела
     * @return созданный отдел DTO
     */
    DepartmentDTO createDepartment(DepartmentDTO t_departmentDTO);

    /**
     * Найти отдел по ID
     * @param t_id ID отдела
     * @return отдел DTO
     */
    DepartmentDTO getDepartmentById(Long t_id);

    /**
     * Обновить данные отдела
     * @param t_id ID отдела
     * @param t_departmentDTO DTO с новыми данными
     * @return обновленный отдел DTO
     */
    DepartmentDTO updateDepartment(Long t_id, DepartmentDTO t_departmentDTO);

    /**
     * Удалить отдел
     * @param t_id ID отдела
     */
    void deleteDepartment(Long t_id);

    /**
     * Получить все активные отделы
     * @return список отделов DTO
     */
    List<DepartmentDTO> getAllDepartments();

    /**
     * Найти отдел по названию
     * @param t_name название
     * @return отдел DTO
     */
    DepartmentDTO getDepartmentByName(String t_name);

    /**
     * Назначить руководителя отдела
     * @param t_departmentId ID отдела
     * @param t_employeeId ID сотрудника
     * @return обновленный отдел DTO
     */
    DepartmentDTO setDepartmentHead(Long t_departmentId, Long t_employeeId);

    /**
     * Получить статистику по отделу
     * @param t_departmentId ID отдела
     * @return статистика отдела
     */
    DepartmentStatsDTO getDepartmentStats(Long t_departmentId);

    /**
     * Проверить существование отдела по названию
     * @param t_name название
     * @return true если существует
     */
    boolean departmentExists(String t_name);
}
