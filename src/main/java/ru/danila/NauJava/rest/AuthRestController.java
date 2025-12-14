package ru.danila.NauJava.rest;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.danila.NauJava.dto.ErrorResponseDTO;
import ru.danila.NauJava.dto.UserRegistrationDTO;
import ru.danila.NauJava.entity.User;
import ru.danila.NauJava.service.UserService;

/**
 * REST контроллер для управления пользователями и аутентификацией
 * Предоставляет endpoints для регистрации, логина и управления аккаунтами
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    private final UserService m_userService;

    @Autowired
    public AuthRestController(UserService t_userService) {
        this.m_userService = t_userService;
    }

    /**
     * Регистрация нового пользователя
     * @param t_registrationDTO данные для регистрации
     * @return регистрационные данные пользователя
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDTO t_registrationDTO) {
        log.info("POST /api/auth/register - регистрация пользователя: {}", t_registrationDTO.getUsername());
        
        try {
            User s_user = m_userService.registerUser(t_registrationDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Пользователь " + s_user.getM_username() + " успешно зарегистрирован");
        } catch (IllegalArgumentException e) {
            log.warn("Ошибка при регистрации: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponseDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null));
        }
    }

    /**
     * Получить информацию о пользователе по ID
     * @param t_userId идентификатор пользователя
     * @return данные пользователя
     */
    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") Long t_userId) {
        log.info("GET /api/auth/user/{} - получение информации о пользователе", t_userId);
        
        try {
            User s_user = m_userService.getUserById(t_userId);
            return ResponseEntity.ok(s_user);
        } catch (Exception e) {
            log.warn("Пользователь не найден: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponseDTO(HttpStatus.NOT_FOUND.value(), "Пользователь не найден", null));
        }
    }

    /**
     * Изменить пароль пользователя
     * @param t_userId идентификатор пользователя
     * @param t_oldPassword старый пароль
     * @param t_newPassword новый пароль
     * @return сообщение об успешном изменении
     */
    @PostMapping("/change-password/{id}")
    public ResponseEntity<?> changePassword(
            @PathVariable("id") Long t_userId,
            @RequestParam("oldPassword") String t_oldPassword,
            @RequestParam("newPassword") String t_newPassword) {
        log.info("POST /api/auth/change-password/{} - изменение пароля пользователя", t_userId);
        
        try {
            m_userService.changePassword(t_userId, t_oldPassword, t_newPassword);
            return ResponseEntity.ok("Пароль успешно изменен");
        } catch (IllegalArgumentException e) {
            log.warn("Ошибка при изменении пароля: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponseDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null));
        } catch (Exception e) {
            log.warn("Ошибка при изменении пароля: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponseDTO(HttpStatus.NOT_FOUND.value(), "Пользователь не найден", null));
        }
    }

    /**
     * Активировать/деактивировать аккаунт пользователя
     * @param t_userId идентификатор пользователя
     * @param t_enabled статус активности
     * @return сообщение об успешном изменении
     */
    @PutMapping("/user/{id}/enabled")
    public ResponseEntity<?> setUserEnabled(
            @PathVariable("id") Long t_userId,
            @RequestParam("enabled") boolean t_enabled) {
        log.info("PUT /api/auth/user/{}/enabled - изменение статуса активности пользователя", t_userId);
        
        try {
            m_userService.setUserEnabled(t_userId, t_enabled);
            String s_message = t_enabled ? "Аккаунт активирован" : "Аккаунт деактивирован";
            return ResponseEntity.ok(s_message);
        } catch (Exception e) {
            log.warn("Ошибка при изменении статуса активности: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponseDTO(HttpStatus.NOT_FOUND.value(), "Пользователь не найден", null));
        }
    }

    /**
     * Проверить доступность имени пользователя
     * @param t_username имя пользователя
     * @return результат проверки
     */
    @GetMapping("/check-username")
    public ResponseEntity<?> checkUsernameAvailability(@RequestParam("username") String t_username) {
        log.info("GET /api/auth/check-username - проверка доступности имени: {}", t_username);
        
        boolean s_available = !m_userService.usernameExists(t_username);
        return ResponseEntity.ok(new AvailabilityResponse(t_username, s_available));
    }

    /**
     * Проверить доступность email
     * @param t_email email
     * @return результат проверки
     */
    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmailAvailability(@RequestParam("email") String t_email) {
        log.info("GET /api/auth/check-email - проверка доступности email: {}", t_email);
        
        boolean s_available = !m_userService.emailExists(t_email);
        return ResponseEntity.ok(new AvailabilityResponse(t_email, s_available));
    }

    /**
     * Вспомогательный класс для ответа проверки доступности
     */
    @lombok.Data
    @lombok.AllArgsConstructor
    public static class AvailabilityResponse {
        private String value;
        private boolean available;
    }
}
