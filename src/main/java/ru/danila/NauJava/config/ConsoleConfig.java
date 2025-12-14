package ru.danila.NauJava.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация для работы с консолью
 * Запускает интерактивный режим работы с приложением
 */
@Configuration
public class ConsoleConfig {

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
        };
    }
}