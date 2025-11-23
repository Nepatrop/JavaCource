package ru.danila.NauJava.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.danila.NauJava.dao.EmployeeRepository;
import ru.danila.NauJava.dao.UserRepository;
import ru.danila.NauJava.entity.Employee;
import ru.danila.NauJava.entity.User;

/**
 * Тестовый инициализатор данных без интерактивного режима
 */
@Configuration
@Profile("test")
public class TestDataInitializer {

    @Bean
    public CommandLineRunner testDataLoader(EmployeeRepository employeeRepository,
                                            UserRepository userRepository,
                                            PasswordEncoder passwordEncoder) {
        return args -> {
            System.out.println("Инициализация тестовых данных...");

            // Очищаем существующие данные
            employeeRepository.findAll().clear();
            userRepository.findAll().clear();

            // Добавляем тестовых сотрудников
            Employee emp1 = new Employee();
            emp1.setId(1L);
            emp1.setFirstName("Тест");
            emp1.setLastName("Сотрудник");
            emp1.setDepartment("IT");
            emp1.setPosition("Разработчик");
            employeeRepository.create(emp1);

            // Добавляем тестовых пользователей
            User admin = new User();
            admin.setId(1L);
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.addRole("ADMIN");
            userRepository.create(admin);

            User user = new User();
            user.setId(2L);
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user123"));
            user.addRole("USER");
            userRepository.create(user);

            System.out.println("Тестовые данные инициализированы");
        };
    }
}
