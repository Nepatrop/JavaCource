package ru.danila.NauJava.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.danila.NauJava.repository.EmployeeRepository;
import ru.danila.NauJava.repository.UserRepository;
import ru.danila.NauJava.entity.Department;
import ru.danila.NauJava.entity.Employee;
import ru.danila.NauJava.entity.Role;
import ru.danila.NauJava.entity.User;
import ru.danila.NauJava.repository.DepartmentRepository;
import ru.danila.NauJava.repository.RoleRepository;

/**
 *  Инициализация тестовых данных при запуске приложения
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final EmployeeRepository m_employeeRepository;
    private final UserRepository m_userRepository;
    private final DepartmentRepository m_departmentRepository;
    private final RoleRepository m_roleRepository;
    private final PasswordEncoder m_passwordEncoder;

    @Autowired
    public DataInitializer(EmployeeRepository t_employeeRepository,
                           UserRepository t_userRepository,
                           DepartmentRepository t_departmentRepository,
                           RoleRepository t_roleRepository,
                           PasswordEncoder t_passwordEncoder) {
        this.m_employeeRepository = t_employeeRepository;
        this.m_userRepository = t_userRepository;
        this.m_departmentRepository = t_departmentRepository;
        this.m_roleRepository = t_roleRepository;
        this.m_passwordEncoder = t_passwordEncoder;
    }

    @Override
    public void run(String... t_args) throws Exception {
        // Добавляем тестовых сотрудников через репозиторий
        addTestEmployees();
        addTestUsers();
    }

    private void addTestEmployees() {
        // Проверяем, нет ли уже отделов (более надёжный способ проверки инициализации)
        long s_deptCount = m_departmentRepository.count();
        if (s_deptCount > 0) {
            return; // Данные уже инициализированы
        }

        // Создаем и сохраняем все отделы в одной операции
        Department s_dept_it = m_departmentRepository.save(new Department("IT", "Отдел информационных технологий"));
        Department s_dept_hr = m_departmentRepository.save(new Department("HR", "Отдел кадров"));
        Department s_dept_finance = m_departmentRepository.save(new Department("Финансы", "Финансовый отдел"));
        Department s_dept_marketing = m_departmentRepository.save(new Department("Маркетинг", "Маркетинговый отдел"));
        
        // Убеждаемся, что отделы сохранены в БД перед созданием сотрудников
        m_departmentRepository.flush();

        // Сотрудник 1 - IT
        Employee emp1 = new Employee();
        emp1.setFirstName("Иван");
        emp1.setLastName("Петров");
        emp1.setDepartment(s_dept_it);
        emp1.setEmail("admin@company.com");
        emp1.setPosition("Разработчик");
        m_employeeRepository.save(emp1);

        // Сотрудник 2 - HR
        Employee emp2 = new Employee();
        emp2.setFirstName("Мария");
        emp2.setLastName("Сидорова");
        emp2.setDepartment(s_dept_hr);
        emp2.setEmail("maria.sidorova@company.com");
        emp2.setPosition("Менеджер");
        m_employeeRepository.save(emp2);

        // Сотрудник 3 - Финансы
        Employee emp3 = new Employee();
        emp3.setFirstName("Алексей");
        emp3.setLastName("Козлов");
        emp3.setDepartment(s_dept_finance);
        emp3.setEmail("alexey.kozlov@company.com");
        emp3.setPosition("Бухгалтер");
        m_employeeRepository.save(emp3);

        // Сотрудник 4 - IT
        Employee emp4 = new Employee();
        emp4.setFirstName("Анна");
        emp4.setLastName("Иванова");
        emp4.setDepartment(s_dept_it);
        emp4.setEmail("anna.ivanova@company.com");
        emp4.setPosition("Тестировщик");
        m_employeeRepository.save(emp4);

        // Сотрудник 5 - Маркетинг
        Employee emp5 = new Employee();
        emp5.setFirstName("Дмитрий");
        emp5.setLastName("Смирнов");
        emp5.setDepartment(s_dept_marketing);
        emp5.setEmail("dmitry.smirnov@company.com");
        emp5.setPosition("Аналитик");
        m_employeeRepository.save(emp5);
    }

    private void addTestUsers() {
        long s_roleCount = m_roleRepository.count();
        if (s_roleCount > 0) {
            return; // Данные уже инициализированы
        }

        // Создаем и сохраняем роли
        Role s_admin_role = m_roleRepository.save(new Role("ADMIN", "Администратор системы"));
        Role s_user_role = m_roleRepository.save(new Role("USER", "Обычный пользователь"));
        m_roleRepository.flush();

        // Находим сотрудников для привязки
        Employee s_adminEmployee = m_employeeRepository.findAll().stream()
                .filter(e -> "ivan.petrov@company.com".equals(e.getM_email()))
                .findFirst().orElse(null);
        
        Employee s_userEmployee = m_employeeRepository.findAll().stream()
                .filter(e -> "maria.sidorova@company.com".equals(e.getM_email()))
                .findFirst().orElse(null);

        // Администратор
        User s_admin = new User();
        s_admin.setUsername("admin");
        s_admin.setEmail("admin@company.com");
        s_admin.setPassword(m_passwordEncoder.encode("admin"));
        s_admin.addRole(s_admin_role);
        s_admin.setM_employee(s_adminEmployee);
        m_userRepository.save(s_admin);

        // Обычный пользователь
        User s_user = new User();
        s_user.setUsername("user");
        s_user.setEmail("user@company.com");
        s_user.setPassword(m_passwordEncoder.encode("user"));
        s_user.addRole(s_user_role);
        s_user.setM_employee(s_userEmployee);
        m_userRepository.save(s_user);
        m_userRepository.flush();
    }
}