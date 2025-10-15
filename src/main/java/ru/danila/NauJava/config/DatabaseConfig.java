package ru.danila.NauJava.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.danila.NauJava.entity.Employee;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс конфигурации базы данных
 * Создает бин-контейнер для хранения сотрудников
 */
@Configuration
public class DatabaseConfig {

    @Bean
    public List<Employee> employeeContainer() {
        // Создаем и возвращаем пустой список для хранения сотрудников
        return new ArrayList<>();
    }
}