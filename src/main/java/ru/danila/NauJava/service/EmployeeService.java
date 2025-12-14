package ru.danila.NauJava.service;

import ru.danila.NauJava.dto.EmployeeDTO;
import ru.danila.NauJava.entity.Employee;

import java.util.List;

/**
 * Интерфейс сервиса для работы с сотрудниками
 * Определяет методы бизнес-логики управления сотрудниками
 */
public interface EmployeeService {
    
    /**
     * Создать нового сотрудника
     * @param t_employeeDTO DTO с данными сотрудника
     * @return созданный сотрудник DTO
     */
    EmployeeDTO createEmployee(EmployeeDTO t_employeeDTO);

    /**
     * Найти сотрудника по ID
     * @param t_id ID сотрудника
     * @return сотрудник DTO
     */
    EmployeeDTO getEmployeeById(Long t_id);

    /**
     * Обновить данные сотрудника
     * @param t_id ID сотрудника
     * @param t_employeeDTO DTO с новыми данными
     * @return обновленный сотрудник DTO
     */
    EmployeeDTO updateEmployee(Long t_id, EmployeeDTO t_employeeDTO);

    /**
     * Удалить сотрудника (мягкое удаление)
     * @param t_id ID сотрудника
     */
    void deleteEmployee(Long t_id);

    /**
     * Получить всех активных сотрудников
     * @return список DTO сотрудников
     */
    List<EmployeeDTO> getAllEmployees();

    /**
     * Найти сотрудников по отделу
     * @param t_departmentId ID отдела
     * @return список DTO сотрудников
     */
    List<EmployeeDTO> getEmployeesByDepartment(Long t_departmentId);

    /**
     * Поиск сотрудников по имени
     * @param t_firstName имя
     * @return список DTO сотрудников
     */
    List<EmployeeDTO> searchEmployeesByFirstName(String t_firstName);

    /**
     * Поиск сотрудников по фамилии
     * @param t_lastName фамилия
     * @return список DTO сотрудников
     */
    List<EmployeeDTO> searchEmployeesByLastName(String t_lastName);

    /**
     * Проверить существование сотрудника с такой же электронной почтой
     * @param t_email email
     * @param t_excludeId ID для исключения из поиска (при обновлении)
     * @return true если существует
     */
    boolean emailExists(String t_email, Long t_excludeId);

    /**
     * Нанять нового сотрудника (альтернативный метод создания)
     * @param t_departmentId ID отдела
     * @param t_firstName имя
     * @param t_lastName фамилия
     * @param t_email email
     * @param t_position должность
     * @return созданный сотрудник DTO
     */
    EmployeeDTO hireEmployee(Long t_departmentId, String t_firstName, String t_lastName, 
                             String t_email, String t_position);

    /**
     * Найти сотрудника по ID (альтернативный метод)
     * @param t_id ID сотрудника
     * @return сотрудник DTO
     */
    EmployeeDTO findEmployeeById(Long t_id);

    /**
     * Обновить должность сотрудника
     * @param t_employeeId ID сотрудника
     * @param t_newPosition новая должность
     * @return обновленный сотрудник DTO
     */
    EmployeeDTO updateEmployeePosition(Long t_employeeId, String t_newPosition);

    /**
     * Перевести сотрудника в другой отдел
     * @param t_employeeId ID сотрудника
     * @param t_departmentName название отдела
     * @return обновленный сотрудник DTO
     */
    EmployeeDTO transferEmployeeDepartment(Long t_employeeId, String t_departmentName);

    /**
     * Найти сущность Employee по ID (для внутреннего использования)
     * @param t_id ID сотрудника
     * @return сущность Employee или null
     */
    Employee findEntityById(Long t_id);
}