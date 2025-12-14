package ru.danila.NauJava.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для регистрации пользователя
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDTO {
    
    /**
     * Имя пользователя
     */
    @NotBlank(message = "Имя пользователя не может быть пустым")
    @Size(min = 3, max = 50, message = "Имя пользователя должно быть от 3 до 50 символов")
    private String m_username = "";

    /**
     * Email
     */
    @Email(message = "Email должен быть валидным")
    @NotBlank(message = "Email не может быть пустым")
    private String m_email = "";

    /**
     * Пароль
     */
    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 6, max = 100, message = "Пароль должен быть от 6 до 100 символов")
    private String m_password = "";

    /**
     * Подтверждение пароля
     */
    @NotBlank(message = "Подтверждение пароля не может быть пустым")
    private String m_confirmPassword = "";

    /**
     * Получить имя пользователя
     */
    public String getUsername() {
        return m_username;
    }

    /**
     * Получить email
     */
    public String getEmail() {
        return m_email;
    }

    /**
     * Получить пароль
     */
    public String getPassword() {
        return m_password;
    }

    /**
     * Получить подтверждение пароля
     */
    public String getConfirmPassword() {
        return m_confirmPassword;
    }
}
