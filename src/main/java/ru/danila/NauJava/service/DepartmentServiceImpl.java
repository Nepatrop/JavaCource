package ru.danila.NauJava.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.danila.NauJava.dto.DepartmentDTO;
import ru.danila.NauJava.dto.DepartmentStatsDTO;
import ru.danila.NauJava.entity.Department;
import ru.danila.NauJava.entity.Employee;
import ru.danila.NauJava.exception.ResourceNotFoundException;
import ru.danila.NauJava.repository.DepartmentRepository;
import ru.danila.NauJava.repository.EmployeeRepository;

import java.util.List;

/**
 * Реализация сервиса для работы с отделами
 * Управляет отделами и предоставляет статистику
 */
@Slf4j
@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository m_departmentRepository;
    private final EmployeeRepository m_employeeRepository;

    /**
     * Конструктор с внедрением зависимостей
     */
    @Autowired
    public DepartmentServiceImpl(DepartmentRepository t_departmentRepository,
                               EmployeeRepository t_employeeRepository) {
        this.m_departmentRepository = t_departmentRepository;
        this.m_employeeRepository = t_employeeRepository;
    }

    /**
     * Преобразовать Department в DepartmentDTO
     */
    private DepartmentDTO convertToDTO(Department t_department) {
        DepartmentDTO s_dto = new DepartmentDTO();
        s_dto.setM_id(t_department.getM_id());
        s_dto.setM_name(t_department.getM_name());
        s_dto.setM_description(t_department.getM_description());
        
        if (t_department.getM_head() != null) {
            s_dto.setM_headId(t_department.getM_head().getM_id());
            s_dto.setM_headName(t_department.getM_head().getFullName());
        }
        
        // Используем countEmployeesByDepartment вместо обращения к ленивой коллекции
        s_dto.setM_employeeCount(m_departmentRepository.countEmployeesByDepartment(t_department.getM_id()));
        return s_dto;
    }

    @Transactional(readOnly = true)
    private Department getDepartmentEntityById(Long t_id) {
        return m_departmentRepository.findById(t_id)
                .orElseThrow(() -> new ResourceNotFoundException("Отдел с ID " + t_id + " не найден"));
    }

    @Override
    public DepartmentDTO createDepartment(DepartmentDTO t_departmentDTO) {
        
        // Проверка на существование отдела с таким названием
        if (m_departmentRepository.existsByM_nameIgnoreCase(t_departmentDTO.getM_name())) {
            throw new IllegalArgumentException("Отдел с таким названием уже существует");
        }

        Department s_department = new Department();
        s_department.setM_name(t_departmentDTO.getM_name());
        s_department.setM_description(t_departmentDTO.getM_description());

        // Установить руководителя, если указан
        if (t_departmentDTO.getM_headId() != null) {
            Employee s_head = m_employeeRepository.findById(t_departmentDTO.getM_headId())
                    .orElseThrow(() -> new ResourceNotFoundException("Руководитель не найден"));
            s_department.setM_head(s_head);
        }

        Department s_result = m_departmentRepository.save(s_department);
        return convertToDTO(s_result);
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentDTO getDepartmentById(Long t_id) {
        return convertToDTO(getDepartmentEntityById(t_id));
    }

    @Override
    public DepartmentDTO updateDepartment(Long t_id, DepartmentDTO t_departmentDTO) {
        
        Department s_department = getDepartmentEntityById(t_id);

        // Проверка названия (если изменилось)
        if (!s_department.getM_name().equalsIgnoreCase(t_departmentDTO.getM_name()) 
            && m_departmentRepository.existsByM_nameIgnoreCase(t_departmentDTO.getM_name())) {
            throw new IllegalArgumentException("Отдел с таким названием уже существует");
        }

        s_department.setM_name(t_departmentDTO.getM_name());
        s_department.setM_description(t_departmentDTO.getM_description());

        Department s_updated = m_departmentRepository.save(s_department);
        return convertToDTO(s_updated);
    }

    @Override
    public void deleteDepartment(Long t_id) {
        
        Department s_department = getDepartmentEntityById(t_id);
        
        // Проверить, есть ли сотрудники в отделе
        long s_employeeCount = m_departmentRepository.countEmployeesByDepartment(t_id);
        if (s_employeeCount > 0) {
            throw new IllegalStateException("Невозможно удалить отдел, в котором есть сотрудники");
        }

        s_department.setM_isDeleted(true);
        m_departmentRepository.save(s_department);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentDTO> getAllDepartments() {
        return m_departmentRepository.findAllActive().stream()
                .map(this::convertToDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentDTO getDepartmentByName(String t_name) {
        Department s_department = m_departmentRepository.findByM_nameIgnoreCase(t_name)
                .orElseThrow(() -> new ResourceNotFoundException("Отдел '" + t_name + "' не найден"));
        return convertToDTO(s_department);
    }

    @Override
    public DepartmentDTO setDepartmentHead(Long t_departmentId, Long t_employeeId) {
        
        Department s_department = getDepartmentEntityById(t_departmentId);
        Employee s_head = m_employeeRepository.findById(t_employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Сотрудник не найден"));

        // Проверить, что сотрудник работает в этом отделе
        if (!s_head.getM_department().getM_id().equals(t_departmentId)) {
            throw new IllegalArgumentException("Сотрудник должен работать в этом отделе");
        }

        s_department.setM_head(s_head);
        Department s_updated = m_departmentRepository.save(s_department);
        return convertToDTO(s_updated);
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentStatsDTO getDepartmentStats(Long t_departmentId) {
        
        Department s_department = getDepartmentEntityById(t_departmentId);
        long s_employeeCount = m_departmentRepository.countEmployeesByDepartment(t_departmentId);

        DepartmentStatsDTO s_stats = new DepartmentStatsDTO();
        s_stats.setM_departmentId(t_departmentId);
        s_stats.setM_departmentName(s_department.getM_name());
        s_stats.setM_employeeCount(s_employeeCount);
        
        if (s_department.getM_head() != null) {
            s_stats.setM_headName(s_department.getM_head().getFullName());
        }

        return s_stats;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean departmentExists(String t_name) {
        return m_departmentRepository.existsByM_nameIgnoreCase(t_name);
    }
}
