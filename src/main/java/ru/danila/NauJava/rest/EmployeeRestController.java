package ru.danila.NauJava.rest;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.danila.NauJava.dto.EmployeeDTO;
import ru.danila.NauJava.service.EmployeeService;

import java.util.List;

/**
 * REST контроллер для управления сотрудниками
 * Предоставляет endpoints для CRUD операций с сотрудниками
 */
@Slf4j
@RestController
@RequestMapping("/api/employees")
public class EmployeeRestController {

    private final EmployeeService m_employeeService;

    @Autowired
    public EmployeeRestController(EmployeeService t_employeeService) {
        this.m_employeeService = t_employeeService;
    }

    /**
     * Получить всех сотрудников
     * @return список всех сотрудников
     */
    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        log.info("GET /api/employees - получение всех сотрудников");
        List<EmployeeDTO> s_employees = m_employeeService.getAllEmployees();
        return ResponseEntity.ok(s_employees);
    }

    /**
     * Получить сотрудника по ID
     * @param t_id идентификатор сотрудника
     * @return сотрудник с указанным ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable("id") Long t_id) {
        log.info("GET /api/employees/{} - получение сотрудника", t_id);
        EmployeeDTO s_employee = m_employeeService.getEmployeeById(t_id);
        return ResponseEntity.ok(s_employee);
    }

    /**
     * Создать нового сотрудника
     * @param t_employeeDTO данные нового сотрудника
     * @return созданный сотрудник
     */
    @PostMapping
    public ResponseEntity<EmployeeDTO> createEmployee(@Valid @RequestBody EmployeeDTO t_employeeDTO) {
        log.info("POST /api/employees - создание нового сотрудника: {}", t_employeeDTO.getFirstName());
        EmployeeDTO s_createdEmployee = m_employeeService.createEmployee(t_employeeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(s_createdEmployee);
    }

    /**
     * Обновить данные сотрудника
     * @param t_id идентификатор сотрудника
     * @param t_employeeDTO новые данные
     * @return обновленный сотрудник
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(
            @PathVariable("id") Long t_id,
            @Valid @RequestBody EmployeeDTO t_employeeDTO) {
        log.info("PUT /api/employees/{} - обновление сотрудника", t_id);
        EmployeeDTO s_updatedEmployee = m_employeeService.updateEmployee(t_id, t_employeeDTO);
        return ResponseEntity.ok(s_updatedEmployee);
    }

    /**
     * Удалить сотрудника
     * @param t_id идентификатор сотрудника
     * @return пустой ответ
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable("id") Long t_id) {
        log.info("DELETE /api/employees/{} - удаление сотрудника", t_id);
        m_employeeService.deleteEmployee(t_id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Получить сотрудников по отделу
     * @param t_departmentId идентификатор отдела
     * @return список сотрудников отдела
     */
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByDepartment(
            @PathVariable("departmentId") Long t_departmentId) {
        log.info("GET /api/employees/department/{} - получение сотрудников по отделу", t_departmentId);
        List<EmployeeDTO> s_employees = m_employeeService.getEmployeesByDepartment(t_departmentId);
        return ResponseEntity.ok(s_employees);
    }

    /**
     * Поиск сотрудников по имени
     * @param t_firstName имя для поиска
     * @return список найденных сотрудников
     */
    @GetMapping("/search/first-name")
    public ResponseEntity<List<EmployeeDTO>> searchByFirstName(
            @RequestParam("name") String t_firstName) {
        log.info("GET /api/employees/search/first-name - поиск по имени: {}", t_firstName);
        List<EmployeeDTO> s_employees = m_employeeService.searchEmployeesByFirstName(t_firstName);
        return ResponseEntity.ok(s_employees);
    }

    /**
     * Поиск сотрудников по фамилии
     * @param t_lastName фамилия для поиска
     * @return список найденных сотрудников
     */
    @GetMapping("/search/last-name")
    public ResponseEntity<List<EmployeeDTO>> searchByLastName(
            @RequestParam("name") String t_lastName) {
        log.info("GET /api/employees/search/last-name - поиск по фамилии: {}", t_lastName);
        List<EmployeeDTO> s_employees = m_employeeService.searchEmployeesByLastName(t_lastName);
        return ResponseEntity.ok(s_employees);
    }
}