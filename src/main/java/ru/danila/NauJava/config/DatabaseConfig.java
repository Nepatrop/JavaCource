package ru.danila.NauJava.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.danila.NauJava.entity.*;
import ru.danila.NauJava.repository.DepartmentRepository;
import ru.danila.NauJava.repository.EmployeeRepository;
import ru.danila.NauJava.repository.RoleRepository;
import ru.danila.NauJava.repository.UserRepository;

import java.time.LocalDate;

/**
 * Класс конфигурации базы данных и инициализации начальных данных
 * Создает роли, отделы и тестовых пользователей при запуске
 */
@Configuration
public class DatabaseConfig {

    /**
     * Инициализация начальных данных в БД при запуске приложения
     */
    @Bean
    public CommandLineRunner initializeData(
            RoleRepository t_roleRepository,
            DepartmentRepository t_departmentRepository,
            EmployeeRepository t_employeeRepository,
            UserRepository t_userRepository,
            PasswordEncoder t_passwordEncoder) {
        
        return t_args -> {
            // Инициализируем роли
            initializeRoles(t_roleRepository);
            
            // Инициализируем отделы
            initializeDepartments(t_departmentRepository);
            
            // Инициализируем сотрудников
            initializeEmployees(t_departmentRepository, t_employeeRepository);
            
            // Инициализируем пользователей
            initializeUsers(t_roleRepository, t_employeeRepository, t_userRepository, t_passwordEncoder);
        };
    }

    /**
     * Инициализация ролей
     */
    private void initializeRoles(RoleRepository t_roleRepository) {
        if (t_roleRepository.count() == 0) {
            Role s_adminRole = new Role("ROLE_ADMIN", "Администратор системы");
            Role s_userRole = new Role("ROLE_USER", "Обычный пользователь");
            
            t_roleRepository.save(s_adminRole);
            t_roleRepository.save(s_userRole);
        }
    }

    /**
     * Инициализация отделов
     */
    private void initializeDepartments(DepartmentRepository t_departmentRepository) {
        if (t_departmentRepository.count() == 0) {
            Department s_itDept = new Department();
            s_itDept.setM_name("IT");
            s_itDept.setM_description("Отдел информационных технологий");
            t_departmentRepository.save(s_itDept);
            
            Department s_hrDept = new Department();
            s_hrDept.setM_name("HR");
            s_hrDept.setM_description("Отдел кадров");
            t_departmentRepository.save(s_hrDept);
            
            Department s_financeDept = new Department();
            s_financeDept.setM_name("Финансы");
            s_financeDept.setM_description("Финансовый отдел");
            t_departmentRepository.save(s_financeDept);
        }
    }

    /**
     * Инициализация сотрудников
     */
    private void initializeEmployees(
            DepartmentRepository t_departmentRepository,
            EmployeeRepository t_employeeRepository) {
        
        if (t_employeeRepository.count() == 0) {
            Department s_itDept = t_departmentRepository.findByM_nameIgnoreCase("IT").orElse(null);
            Department s_hrDept = t_departmentRepository.findByM_nameIgnoreCase("HR").orElse(null);
            Department s_financeDept = t_departmentRepository.findByM_nameIgnoreCase("Финансы").orElse(null);
            
            // Сотрудник 1 - IT разработчик
            Employee s_emp1 = new Employee();
            s_emp1.setM_firstName("Иван");
            s_emp1.setM_lastName("Петров");
            s_emp1.setM_email("admin@company.com");
            s_emp1.setM_phone("+7-900-100-01-01");
            s_emp1.setM_position("Разработчик");
            s_emp1.setM_hireDate(LocalDate.of(2023, 1, 15));
            s_emp1.setM_department(s_itDept);
            t_employeeRepository.save(s_emp1);
            
            // Сотрудник 2 - HR менеджер
            Employee s_emp2 = new Employee();
            s_emp2.setM_firstName("Мария");
            s_emp2.setM_lastName("Сидорова");
            s_emp2.setM_email("maria.sidorova@company.com");
            s_emp2.setM_phone("+7-900-100-01-02");
            s_emp2.setM_position("Менеджер по персоналу");
            s_emp2.setM_hireDate(LocalDate.of(2022, 6, 1));
            s_emp2.setM_department(s_hrDept);
            t_employeeRepository.save(s_emp2);
            
            // Сотрудник 3 - Finance бухгалтер
            Employee s_emp3 = new Employee();
            s_emp3.setM_firstName("Алексей");
            s_emp3.setM_lastName("Козлов");
            s_emp3.setM_email("alexey.kozlov@company.com");
            s_emp3.setM_phone("+7-900-100-01-03");
            s_emp3.setM_position("Бухгалтер");
            s_emp3.setM_hireDate(LocalDate.of(2023, 3, 10));
            s_emp3.setM_department(s_financeDept);
            t_employeeRepository.save(s_emp3);
            
            // Сотрудник 4 - IT тестировщик
            Employee s_emp4 = new Employee();
            s_emp4.setM_firstName("Анна");
            s_emp4.setM_lastName("Иванова");
            s_emp4.setM_email("anna.ivanova@company.com");
            s_emp4.setM_phone("+7-900-100-01-04");
            s_emp4.setM_position("QA тестировщик");
            s_emp4.setM_hireDate(LocalDate.of(2023, 5, 20));
            s_emp4.setM_department(s_itDept);
            t_employeeRepository.save(s_emp4);
        }
    }

    /**
     * Инициализация пользователей
     */
    private void initializeUsers(
            RoleRepository t_roleRepository,
            EmployeeRepository t_employeeRepository,
            UserRepository t_userRepository,
            PasswordEncoder t_passwordEncoder) {
        
        if (t_userRepository.count() == 0) {
            Role s_adminRole = t_roleRepository.findByM_name("ROLE_ADMIN").orElse(null);
            Role s_userRole = t_roleRepository.findByM_name("ROLE_USER").orElse(null);
            Employee s_emp1 = t_employeeRepository.findByFirstNameContaining("Иван").get(0);
            Employee s_emp2 = t_employeeRepository.findByFirstNameContaining("Мария").get(0);
            
            // Администратор
            User s_admin = new User();
            s_admin.setM_username("admin");
            s_admin.setM_password(t_passwordEncoder.encode("admin123"));
            s_admin.setM_email("admin@company.com");
            s_admin.setM_isEnabled(true);
            s_admin.addRole(s_adminRole);
            s_admin.setM_employee(s_emp1);
            t_userRepository.save(s_admin);
            
            // Обычный пользователь
            User s_user = new User();
            s_user.setM_username("user");
            s_user.setM_password(t_passwordEncoder.encode("user123"));
            s_user.setM_email("user@company.com");
            s_user.setM_isEnabled(true);
            s_user.addRole(s_userRole);
            s_user.setM_employee(s_emp2);
            t_userRepository.save(s_user);
        }
    }
}