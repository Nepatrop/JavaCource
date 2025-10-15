package ru.danila.NauJava.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

/**
 * Конфигурационный класс приложения
 * Выводит информацию о приложении после инициализации
 */
@Configuration
public class AppConfig {

    // Внедряем значения из application.properties
    @Value("${app.name}")
    private String m_appName = "";

    @Value("${app.version}")
    private String m_appVersion = "";

    /**
     * Выводит информацию о приложении в консоль
     */
    @PostConstruct
    public void printAppInfo() {
        System.out.println("=== " + m_appName + " v" + m_appVersion + " ===");
    }
}