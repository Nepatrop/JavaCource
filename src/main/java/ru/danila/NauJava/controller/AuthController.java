package ru.danila.NauJava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.danila.NauJava.dto.EmployeeDTO;
import ru.danila.NauJava.entity.Role;
import ru.danila.NauJava.entity.User;
import ru.danila.NauJava.repository.DepartmentRepository;
import ru.danila.NauJava.repository.RoleRepository;
import ru.danila.NauJava.service.EmployeeService;
import ru.danila.NauJava.service.UserService;

/**
 * Контроллер для аутентификации и регистрации
 */
@Controller
public class AuthController {

    private final UserService m_userService;
    private final PasswordEncoder m_passwordEncoder;
    private final RoleRepository m_roleRepository;
    private final EmployeeService m_employeeService;
    private final DepartmentRepository m_departmentRepository;

    @Autowired
    public AuthController(UserService t_userService, PasswordEncoder t_passwordEncoder, 
                          RoleRepository t_roleRepository, EmployeeService t_employeeService,
                          DepartmentRepository t_departmentRepository) {
        this.m_userService = t_userService;
        this.m_passwordEncoder = t_passwordEncoder;
        this.m_roleRepository = t_roleRepository;
        this.m_employeeService = t_employeeService;
        this.m_departmentRepository = t_departmentRepository;
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
    @GetMapping("/register")
    public String registrationPage(Model t_model) {
        // Добавляем список отделов для выбора
        t_model.addAttribute("departments", m_departmentRepository.findAllActive());
        return "registration";
    }

    /**
     * POST: Обработка регистрации
     */
    @PostMapping("/register")
    public String registerUser(
            @RequestParam("firstName") String t_firstName,
            @RequestParam("lastName") String t_lastName,
            @RequestParam("username") String t_username,
            @RequestParam("email") String t_email,
            @RequestParam("departmentId") Long t_departmentId,
            @RequestParam("password") String t_password,
            @RequestParam("confirmPassword") String t_confirmPassword,
            Model t_model) {

        // Добавляем отделы в модель для повторного отображения формы при ошибке
        t_model.addAttribute("departments", m_departmentRepository.findAllActive());

        // Проверяем совпадение паролей
        if (!t_password.equals(t_confirmPassword)) {
            t_model.addAttribute("error", "Пароли не совпадают");
            return "registration";
        }

        // Проверяем минимальную длину пароля
        if (t_password.length() < 6) {
            t_model.addAttribute("error", "Пароль должен содержать минимум 6 символов");
            return "registration";
        }

        // Проверяем, существует ли пользователь
        if (m_userService.usernameExists(t_username)) {
            t_model.addAttribute("error", "Пользователь с таким именем уже существует");
            return "registration";
        }

        // Проверяем, существует ли email
        if (m_userService.emailExists(t_email)) {
            t_model.addAttribute("error", "Email уже зарегистрирован");
            return "registration";
        }

        // Создаем нового пользователя
        User s_newUser = new User();
        s_newUser.setUsername(t_username);
        s_newUser.setEmail(t_email);
        s_newUser.setPassword(m_passwordEncoder.encode(t_password)); // Шифруем пароль

        // Получаем роль USER
        Role s_userRole = m_roleRepository.findByM_name("USER")
                .orElse(null);
        
        if (s_userRole != null) {
            s_newUser.addRole(s_userRole);
        }

        s_newUser.setEnabled(true);
        
        // Создаём сотрудника для нового пользователя
        EmployeeDTO s_newEmployeeDTO = new EmployeeDTO();
        s_newEmployeeDTO.setM_firstName(t_firstName);
        s_newEmployeeDTO.setM_lastName(t_lastName);
        s_newEmployeeDTO.setM_email(t_email);
        s_newEmployeeDTO.setM_departmentId(t_departmentId);
        s_newEmployeeDTO.setM_position("Сотрудник");
        EmployeeDTO s_savedEmployee = m_employeeService.createEmployee(s_newEmployeeDTO);
        
        // Привязываем пользователя к сотруднику
        s_newUser.setM_employee(m_employeeService.findEntityById(s_savedEmployee.getM_id()));
        m_userService.saveUser(s_newUser);

        return "redirect:/login?success";
    }

    /**
     * GET: Главная страница
     */
    @GetMapping("/")
    public String homePage() {
        return "index";
    }
}
