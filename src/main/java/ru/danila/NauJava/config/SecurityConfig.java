package ru.danila.NauJava.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Конфигурация Spring Security
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    @Profile("!test") // Основная конфигурация - не для тестов
    public SecurityFilterChain securityFilterChain(HttpSecurity t_http) throws Exception {
        t_http
                .authorizeHttpRequests(authz -> authz
                        // Публичные endpoints - доступны всем
                        .requestMatchers("/", "/login", "/register", "/css/**", "/js/**", "/h2-console/**").permitAll()

                        // Swagger UI и API docs - ТОЛЬКО для ADMIN
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").hasRole("ADMIN")

                        // REST API для auth (регистрация) - публичный
                        .requestMatchers("/api/auth/**").permitAll()

                        // REST API требует аутентификации (любой авторизованный пользователь)
                        .requestMatchers("/api/**").authenticated()

                        // HTML страницы требуют аутентификации (любой авторизованный пользователь)
                        .requestMatchers("/employees/**", "/departments/**", "/reports/**").authenticated()

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")  // Вход по email
                        .passwordParameter("password")
                        .defaultSuccessUrl("/employees", true)
                        .failureUrl("/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                // Отключаем CSRF для упрощения тестирования REST API
                .csrf(AbstractHttpConfigurer::disable)
                // H2 console требует frameOptions
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));

        return t_http.build();
    }

    @Bean
    @Profile("test") // Тестовая конфигурация безопасности
    public SecurityFilterChain testSecurityFilterChain(HttpSecurity t_http) throws Exception {
        t_http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/**").permitAll() // Разрешаем ВСЕ запросы
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/employees/list")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable);

        return t_http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
