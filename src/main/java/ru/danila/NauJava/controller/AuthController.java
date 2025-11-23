package ru.danila.NauJava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.danila.NauJava.entity.User;
import ru.danila.NauJava.service.UserService;

/**
 * Контроллер для аутентификации и регистрации
 */
@Controller
public class AuthController {

    private final UserService m_userService;
    private final PasswordEncoder m_passwordEncoder;

    @Autowired
    public AuthController(UserService t_userService, PasswordEncoder t_passwordEncoder) {
        this.m_userService = t_userService;
        this.m_passwordEncoder = t_passwordEncoder;
    }

    /**
     * GET: Страница логина
     */
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    /**
     * GET: Страница регистрации
     */
    @GetMapping("/registration")
    public String registrationPage() {
        return "registration";
    }

    /**
     * POST: Обработка регистрации (ИСПРАВЛЕННАЯ ВЕРСИЯ)
     */
    @PostMapping("/registration")
    public String registerUser(
            @RequestParam("username") String t_username,
            @RequestParam("password") String t_password,
            @RequestParam(value = "role", defaultValue = "USER") String t_role,
            Model t_model) {

        System.out.println("Регистрация пользователя: " + t_username + ", роль: " + t_role);

        // Проверяем, существует ли пользователь
        if (m_userService.userExists(t_username)) {
            t_model.addAttribute("message", "Пользователь с таким именем уже существует");
            return "registration";
        }

        // Создаем нового пользователя
        User newUser = new User();
        newUser.setUsername(t_username);
        newUser.setPassword(m_passwordEncoder.encode(t_password)); // Шифруем пароль
        newUser.addRole(t_role);
        newUser.setId(System.currentTimeMillis()); // Простой ID

        m_userService.saveUser(newUser);

        t_model.addAttribute("message", "Успешная регистрация!");
        return "login";
    }

    /**
     * GET: Главная страница
     */
    @GetMapping("/")
    public String homePage() {
        return "redirect:/employees/list";
    }
}
