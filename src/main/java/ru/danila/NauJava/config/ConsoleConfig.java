package ru.danila.NauJava.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.danila.NauJava.command.CommandProcessor;

import java.util.Scanner;

/**
 * Конфигурация для работы с консолью
 * Запускает интерактивный режим работы с приложением
 */
@Configuration
public class ConsoleConfig {

    // Зависимость на обработчик команд
    @Autowired
    private CommandProcessor m_commandProcessor;

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
        };
    }
}