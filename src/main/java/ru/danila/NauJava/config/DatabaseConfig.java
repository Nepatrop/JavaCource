package ru.danila.NauJava.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.danila.NauJava.entity.Employee;
import ru.danila.NauJava.entity.Report;
import ru.danila.NauJava.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс конфигурации базы данных
 * Создает бин-контейнеры
 */
@Configuration
public class DatabaseConfig {

    // Создаем и возвращаем пустоые списки
    @Bean
    public List<Employee> employeeContainer() {
        return new ArrayList<>();
    }

    @Bean
    public List<User> userContainer() {
        return new ArrayList<>();
    }

    @Bean
    public List<Report> reportContainer() {
        return new ArrayList<>();
    }
}