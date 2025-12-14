package ru.danila.NauJava.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.danila.NauJava.dto.EmployeeDTO;
import ru.danila.NauJava.entity.Department;
import ru.danila.NauJava.entity.Employee;
import ru.danila.NauJava.entity.Role;
import ru.danila.NauJava.entity.User;
import ru.danila.NauJava.exception.ResourceNotFoundException;
import ru.danila.NauJava.repository.DepartmentRepository;
import ru.danila.NauJava.repository.EmployeeRepository;
import ru.danila.NauJava.repository.RoleRepository;
import ru.danila.NauJava.repository.UserRepository;

import java.util.List;
import java.util.Optional;

/**
 * Реализация сервиса для работы с сотрудниками
 * Содержит основную бизнес-логику управления сотрудниками
 */
@Slf4j
@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository m_employeeRepository;
    private final DepartmentRepository m_departmentRepository;
    private final UserRepository m_userRepository;
    private final RoleRepository m_roleRepository;

    /**
     * Конструктор с внедрением зависимостей
     */
    @Autowired
    public EmployeeServiceImpl(EmployeeRepository t_employeeRepository,
                             DepartmentRepository t_departmentRepository,
                             UserRepository t_userRepository,
                             RoleRepository t_roleRepository) {
        this.m_employeeRepository = t_employeeRepository;
        this.m_departmentRepository = t_departmentRepository;
        this.m_userRepository = t_userRepository;
        this.m_roleRepository = t_roleRepository;
    }

    /**
     * Получить Employee по ID (приватный метод)
     */
    private Employee getEmployeeEntityById(Long t_id) {
        return m_employeeRepository.findById(t_id)
                .orElseThrow(() -> new ResourceNotFoundException("Сотрудник с ID " + t_id + " не найден"));
    }

    /**
     * Преобразовать Employee в EmployeeDTO
     */
    private EmployeeDTO convertToDTO(Employee t_employee) {
        String s_deptName = t_employee.getM_department() != null ? 
                t_employee.getM_department().getM_name() : null;
        
        EmployeeDTO s_dto = new EmployeeDTO();
        s_dto.setM_id(t_employee.getM_id());
        s_dto.setM_firstName(t_employee.getM_firstName());
        s_dto.setM_lastName(t_employee.getM_lastName());
        s_dto.setM_email(t_employee.getM_email());
        s_dto.setM_phone(t_employee.getM_phone());
        s_dto.setM_position(t_employee.getM_position());
        s_dto.setM_departmentId(t_employee.getM_department() != null ? t_employee.getM_department().getM_id() : null);
        s_dto.setM_departmentName(s_deptName);
        
        // Проверить является ли связанный пользователь админом
        Optional<User> s_userOpt = m_userRepository.findByEmployeeId(t_employee.getM_id());
        if (s_userOpt.isPresent()) {
            boolean s_isAdmin = s_userOpt.get().getM_roles().stream()
                    .anyMatch(r -> "ADMIN".equals(r.getM_name()));
            s_dto.setM_isAdmin(s_isAdmin);
        }
        
        return s_dto;
    }

    @Override
    public EmployeeDTO createEmployee(EmployeeDTO t_employeeDTO) {
        
        // Проверка, не существует ли уже сотрудник с такой email
        if (m_employeeRepository.existsByM_emailIgnoreCase(t_employeeDTO.getEmail())) {
            throw new IllegalArgumentException("Сотрудник с такой email уже существует");
        }

        // Получить отдел (необязательно)
        Department s_department = null;
        if (t_employeeDTO.getDepartmentId() != null) {
            s_department = m_departmentRepository.findById(t_employeeDTO.getDepartmentId())
                    .orElse(null);
        }

        // Создать сотрудника
        Employee s_employee = new Employee();
        s_employee.setM_firstName(t_employeeDTO.getFirstName());
        s_employee.setM_lastName(t_employeeDTO.getLastName());
        s_employee.setM_email(t_employeeDTO.getEmail());
        s_employee.setM_phone(t_employeeDTO.getPhone());
        s_employee.setM_position(t_employeeDTO.getPosition());
        s_employee.setM_department(s_department);

        Employee s_result = m_employeeRepository.save(s_employee);
        return convertToDTO(s_result);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeDTO getEmployeeById(Long t_id) {
        Employee s_employee = m_employeeRepository.findById(t_id)
                .orElseThrow(() -> new ResourceNotFoundException("Сотрудник с ID " + t_id + " не найден"));
        return convertToDTO(s_employee);
    }

    @Override
    public EmployeeDTO updateEmployee(Long t_id, EmployeeDTO t_employeeDTO) {
        
        Employee s_employee = m_employeeRepository.findById(t_id)
                .orElseThrow(() -> new ResourceNotFoundException("Сотрудник с ID " + t_id + " не найден"));

        // Проверка email (если изменился)
        if (!s_employee.getM_email().equalsIgnoreCase(t_employeeDTO.getEmail()) 
            && m_employeeRepository.existsByM_emailIgnoreCase(t_employeeDTO.getEmail())) {
            throw new IllegalArgumentException("Этот email уже используется");
        }

        // Получить новый отдел (если указан и изменился)
        if (t_employeeDTO.getDepartmentId() != null) {
            if (s_employee.getM_department() == null || 
                !s_employee.getM_department().getM_id().equals(t_employeeDTO.getDepartmentId())) {
                Department s_newDept = m_departmentRepository.findById(t_employeeDTO.getDepartmentId())
                        .orElseThrow(() -> new ResourceNotFoundException("Отдел не найден"));
                s_employee.setM_department(s_newDept);
            }
        }

        // Обновить поля
        s_employee.setM_firstName(t_employeeDTO.getFirstName());
        s_employee.setM_lastName(t_employeeDTO.getLastName());
        s_employee.setM_email(t_employeeDTO.getEmail());
        s_employee.setM_phone(t_employeeDTO.getPhone());
        s_employee.setM_position(t_employeeDTO.getPosition());

        Employee s_updated = m_employeeRepository.save(s_employee);
        
        // Обновить роль пользователя если есть связанный User
        updateUserRole(t_id, t_employeeDTO.isAdmin());
        
        return convertToDTO(s_updated);
    }
    
    /**
     * Обновить роль пользователя (ADMIN/USER) по ID сотрудника
     */
    private void updateUserRole(Long t_employeeId, boolean t_isAdmin) {
        Optional<User> s_userOpt = m_userRepository.findByEmployeeId(t_employeeId);
        if (s_userOpt.isEmpty()) {
            return;
        }
        
        User s_user = s_userOpt.get();
        Role s_adminRole = m_roleRepository.findByM_name("ADMIN").orElse(null);
        Role s_userRole = m_roleRepository.findByM_name("USER").orElse(null);
        
        if (s_adminRole == null || s_userRole == null) {
            return;
        }
        
        boolean s_hasAdminRole = s_user.getM_roles().stream()
                .anyMatch(r -> "ADMIN".equals(r.getM_name()));
        
        if (t_isAdmin && !s_hasAdminRole) {
            // Добавить роль ADMIN
            s_user.addRole(s_adminRole);
            m_userRepository.save(s_user);
        } else if (!t_isAdmin && s_hasAdminRole) {
            // Убрать роль ADMIN
            s_user.getM_roles().removeIf(r -> "ADMIN".equals(r.getM_name()));
            // Убедиться что есть роль USER
            if (s_user.getM_roles().stream().noneMatch(r -> "USER".equals(r.getM_name()))) {
                s_user.addRole(s_userRole);
            }
            m_userRepository.save(s_user);
        }
    }

    @Override
    public void deleteEmployee(Long t_id) {
        
        Employee s_employee = getEmployeeEntityById(t_id);
        s_employee.setM_isDeleted(true);
        m_employeeRepository.save(s_employee);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> getAllEmployees() {
        List<Employee> s_employees = m_employeeRepository.findAllActive();
        return s_employees.stream().map(this::convertToDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> getEmployeesByDepartment(Long t_departmentId) {
        
        Department s_department = m_departmentRepository.findById(t_departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Отдел не найден"));
        
        List<Employee> s_employees = m_employeeRepository.findByM_department(s_department);
        return s_employees.stream().map(this::convertToDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> searchEmployeesByFirstName(String t_firstName) {
        List<Employee> s_employees = m_employeeRepository.findByFirstNameContaining(t_firstName);
        return s_employees.stream().map(this::convertToDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> searchEmployeesByLastName(String t_lastName) {
        List<Employee> s_employees = m_employeeRepository.findByLastNameContaining(t_lastName);
        return s_employees.stream().map(this::convertToDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean emailExists(String t_email, Long t_excludeId) {
        boolean s_exists = m_employeeRepository.existsByM_emailIgnoreCase(t_email);
        if (s_exists && t_excludeId != null) {
            Employee s_employee = m_employeeRepository.findByM_emailIgnoreCase(t_email).orElse(null);
            return s_employee != null && !s_employee.getM_id().equals(t_excludeId);
        }
        return s_exists;
    }

    @Override
    public EmployeeDTO hireEmployee(Long t_departmentId, String t_firstName, String t_lastName, 
                                    String t_email, String t_position) {
        
        Department s_department = m_departmentRepository.findById(t_departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Отдел не найден"));
        
        Employee s_employee = new Employee();
        s_employee.setFirstName(t_firstName);
        s_employee.setLastName(t_lastName);
        s_employee.setEmail(t_email);
        s_employee.setPosition(t_position);
        s_employee.setDepartment(s_department);
        
        Employee s_saved = m_employeeRepository.save(s_employee);
        return convertToDTO(s_saved);
    }

    @Override
    public EmployeeDTO findEmployeeById(Long t_id) {
        return getEmployeeById(t_id);
    }

    @Override
    public EmployeeDTO updateEmployeePosition(Long t_employeeId, String t_newPosition) {
        
        Employee s_employee = getEmployeeEntityById(t_employeeId);
        s_employee.setPosition(t_newPosition);
        
        Employee s_updated = m_employeeRepository.save(s_employee);
        return convertToDTO(s_updated);
    }

    @Override
    public EmployeeDTO transferEmployeeDepartment(Long t_employeeId, String t_departmentName) {
        
        Employee s_employee = getEmployeeEntityById(t_employeeId);
        List<Department> s_depts = m_departmentRepository.findAll().stream()
                .filter(d -> d.getM_name().equalsIgnoreCase(t_departmentName))
                .toList();
        
        if (s_depts.isEmpty()) {
            throw new ResourceNotFoundException("Отдел не найден: " + t_departmentName);
        }
        
        Department s_newDept = s_depts.get(0);
        s_employee.setDepartment(s_newDept);
        Employee s_updated = m_employeeRepository.save(s_employee);
        return convertToDTO(s_updated);
    }

    @Override
    public Employee findEntityById(Long t_id) {
        return m_employeeRepository.findById(t_id).orElse(null);
    }
}
