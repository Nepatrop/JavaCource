package ru.danila.NauJava.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.danila.NauJava.dao.EmployeeRepository;
import ru.danila.NauJava.dao.UserRepository;
import ru.danila.NauJava.entity.Employee;
import ru.danila.NauJava.entity.User;

/**
 *  Инициализация тестовых данных при запуске приложения
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final EmployeeRepository m_employeeRepository;
    private final UserRepository m_userRepository;
    private final PasswordEncoder m_passwordEncoder;

    @Autowired
    public DataInitializer(EmployeeRepository t_employeeRepository,
                           UserRepository t_userRepository,
                           PasswordEncoder t_passwordEncoder) {
        this.m_employeeRepository = t_employeeRepository;
        this.m_userRepository = t_userRepository;
        this.m_passwordEncoder = t_passwordEncoder;
    }

    @Override
    public void run(String... t_args) throws Exception {
        // Добавляем тестовых сотрудников через репозиторий
        addTestEmployees();
        addTestUsers();
    }

    private void addTestEmployees() {
        // Проверяем, нет ли уже сотрудников
        if (!m_employeeRepository.findAll().isEmpty()) {
            return;
        }

        // Сотрудник 1 - IT
        Employee emp1 = new Employee();
        emp1.setId(1L);
        emp1.setFirstName("Иван");
        emp1.setLastName("Петров");
        emp1.setDepartment("IT");
        emp1.setPosition("Разработчик");
        m_employeeRepository.create(emp1);

        // Сотрудник 2 - HR
        Employee emp2 = new Employee();
        emp2.setId(2L);
        emp2.setFirstName("Мария");
        emp2.setLastName("Сидорова");
        emp2.setDepartment("HR");
        emp2.setPosition("Менеджер");
        m_employeeRepository.create(emp2);

        // Сотрудник 3 - Финансы
        Employee emp3 = new Employee();
        emp3.setId(3L);
        emp3.setFirstName("Алексей");
        emp3.setLastName("Козлов");
        emp3.setDepartment("Финансы");
        emp3.setPosition("Бухгалтер");
        m_employeeRepository.create(emp3);

        // Сотрудник 4 - IT
        Employee emp4 = new Employee();
        emp4.setId(4L);
        emp4.setFirstName("Анна");
        emp4.setLastName("Иванова");
        emp4.setDepartment("IT");
        emp4.setPosition("Тестировщик");
        m_employeeRepository.create(emp4);

        // Сотрудник 5 - Маркетинг
        Employee emp5 = new Employee();
        emp5.setId(5L);
        emp5.setFirstName("Дмитрий");
        emp5.setLastName("Смирнов");
        emp5.setDepartment("Маркетинг");
        emp5.setPosition("Аналитик");
        m_employeeRepository.create(emp5);
    }

    private void addTestUsers() {
        if (!m_userRepository.findAll().isEmpty()) {
            return;
        }

        // Администратор
        User admin = new User();
        admin.setId(1L);
        admin.setUsername("admin");
        admin.setPassword(m_passwordEncoder.encode("admin"));
        admin.addRole("ADMIN");
        m_userRepository.create(admin);

        // Обычный пользователь
        User user = new User();
        user.setId(2L);
        user.setUsername("user");
        user.setPassword(m_passwordEncoder.encode("user"));
        user.addRole("USER");
        m_userRepository.create(user);

        System.out.println("Созданы тестовые пользователи:");
        System.out.println("   - admin/admin (ADMIN)");
        System.out.println("   - user/user (USER)");
    }
}