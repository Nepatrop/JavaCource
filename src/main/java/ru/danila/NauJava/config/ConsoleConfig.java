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
        // Возвращаем лямбду, которая будет выполнена после запуска приложения
        return args -> {
            // Используем try-with-resources для автоматического закрытия Scanner
            try (Scanner scanner = new Scanner(System.in)) {
                System.out.println("Система учета сотрудников запущена. Введите 'help' для списка команд.");

                // Бесконечный цикл для чтения команд
                while (true) {
                    System.out.print("> ");
                    String input = scanner.nextLine();

                    // Выход из приложения
                    if ("exit".equalsIgnoreCase(input.trim())) {
                        System.out.println("Выход из программы...");
                        break;
                    }

                    // Передаем команду на обработку
                    m_commandProcessor.processCommand(input);
                }
            }
        };
    }
}