package ru.danila.NauJava.rest;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.danila.NauJava.dto.DepartmentDTO;
import ru.danila.NauJava.dto.DepartmentStatsDTO;
import ru.danila.NauJava.service.DepartmentService;

import java.util.List;

/**
 * REST контроллер для управления отделами
 * Предоставляет endpoints для CRUD операций с отделами и их статистикой
 */
@Slf4j
@RestController
@RequestMapping("/api/departments")
public class DepartmentRestController {

    private final DepartmentService m_departmentService;

    @Autowired
    public DepartmentRestController(DepartmentService t_departmentService) {
        this.m_departmentService = t_departmentService;
    }

    /**
     * Получить все отделы
     * @return список всех отделов
     */
    @GetMapping
    public ResponseEntity<List<DepartmentDTO>> getAllDepartments() {
        log.info("GET /api/departments - получение всех отделов");
        List<DepartmentDTO> s_departments = m_departmentService.getAllDepartments();
        return ResponseEntity.ok(s_departments);
    }

    /**
     * Получить отдел по ID
     * @param t_id идентификатор отдела
     * @return отдел с указанным ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDTO> getDepartmentById(@PathVariable("id") Long t_id) {
        log.info("GET /api/departments/{} - получение отдела", t_id);
        DepartmentDTO s_department = m_departmentService.getDepartmentById(t_id);
        return ResponseEntity.ok(s_department);
    }

    /**
     * Создать новый отдел
     * @param t_departmentDTO данные нового отдела
     * @return созданный отдел
     */
    @PostMapping
    public ResponseEntity<DepartmentDTO> createDepartment(@Valid @RequestBody DepartmentDTO t_departmentDTO) {
        log.info("POST /api/departments - создание нового отдела: {}", t_departmentDTO.getName());
        DepartmentDTO s_createdDept = m_departmentService.createDepartment(t_departmentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(s_createdDept);
    }

    /**
     * Обновить данные отдела
     * @param t_id идентификатор отдела
     * @param t_departmentDTO новые данные
     * @return обновленный отдел
     */
    @PutMapping("/{id}")
    public ResponseEntity<DepartmentDTO> updateDepartment(
            @PathVariable("id") Long t_id,
            @Valid @RequestBody DepartmentDTO t_departmentDTO) {
        log.info("PUT /api/departments/{} - обновление отдела", t_id);
        DepartmentDTO s_updatedDept = m_departmentService.updateDepartment(t_id, t_departmentDTO);
        return ResponseEntity.ok(s_updatedDept);
    }

    /**
     * Удалить отдел
     * @param t_id идентификатор отдела
     * @return пустой ответ
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable("id") Long t_id) {
        log.info("DELETE /api/departments/{} - удаление отдела", t_id);
        m_departmentService.deleteDepartment(t_id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Получить статистику отдела
     * @param t_id идентификатор отдела
     * @return статистика отдела (количество сотрудников, руководитель)
     */
    @GetMapping("/{id}/stats")
    public ResponseEntity<DepartmentStatsDTO> getDepartmentStats(@PathVariable("id") Long t_id) {
        log.info("GET /api/departments/{}/stats - получение статистики отдела", t_id);
        DepartmentStatsDTO s_stats = m_departmentService.getDepartmentStats(t_id);
        return ResponseEntity.ok(s_stats);
    }

    /**
     * Назначить руководителя отдела
     * @param t_departmentId идентификатор отдела
     * @param t_employeeId идентификатор сотрудника
     * @return обновленный отдел
     */
    @PutMapping("/{departmentId}/head/{employeeId}")
    public ResponseEntity<DepartmentDTO> setDepartmentHead(
            @PathVariable("departmentId") Long t_departmentId,
            @PathVariable("employeeId") Long t_employeeId) {
        log.info("PUT /api/departments/{}/head/{} - назначение руководителя", t_departmentId, t_employeeId);
        DepartmentDTO s_updatedDept = m_departmentService.setDepartmentHead(t_departmentId, t_employeeId);
        return ResponseEntity.ok(s_updatedDept);
    }
}
