package ru.danila.NauJava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.danila.NauJava.entity.User;
import ru.danila.NauJava.entity.Employee;
import ru.danila.NauJava.service.UserService;
import ru.danila.NauJava.service.EmployeeService;
import ru.danila.NauJava.service.DepartmentService;

import java.util.HashMap;
import java.util.Map;

/**
 * Контроллер для работы с профилем пользователя
 */
@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService m_userService;
    private final EmployeeService m_employeeService;
    private final DepartmentService m_departmentService;

    @Autowired
    public ProfileController(UserService t_userService, 
                            EmployeeService t_employeeService,
                            DepartmentService t_departmentService) {
        this.m_userService = t_userService;
        this.m_employeeService = t_employeeService;
        this.m_departmentService = t_departmentService;
    }

    /**
     * GET: Страница профиля
     */
    @GetMapping({"", "/"})
    public String profilePage(Model t_model, Authentication t_auth) {
        String s_email = t_auth.getName();
        User s_user = m_userService.findByEmail(s_email);
        
        boolean s_isAdmin = t_auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        
        t_model.addAttribute("user", s_user);
        t_model.addAttribute("isAdmin", s_isAdmin);
        t_model.addAttribute("departments", m_departmentService.getAllDepartments());
        
        return "profile";
    }

    /**
     * GET: API - Получить данные текущего пользователя
     */
    @GetMapping("/api/current")
    @ResponseBody
    public ResponseEntity<?> getCurrentUser(Authentication t_auth) {
        try {
            String s_email = t_auth.getName();
            User s_user = m_userService.findByEmail(s_email);
            
            if (s_user == null) {
                return ResponseEntity.notFound().build();
            }
            
            boolean s_isAdmin = t_auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
            
            Map<String, Object> s_response = new HashMap<>();
            s_response.put("id", s_user.getId());
            s_response.put("username", s_user.getUsername());
            s_response.put("email", s_user.getEmail());
            s_response.put("isAdmin", s_isAdmin);
            
            // Если есть связанный сотрудник
            Employee s_employee = s_user.getM_employee();
            if (s_employee != null) {
                s_response.put("employeeId", s_employee.getM_id());
                s_response.put("firstName", s_employee.getM_firstName());
                s_response.put("lastName", s_employee.getM_lastName());
                s_response.put("position", s_employee.getM_position());
                if (s_employee.getM_department() != null) {
                    s_response.put("departmentId", s_employee.getM_department().getM_id());
                    s_response.put("departmentName", s_employee.getM_department().getM_name());
                }
            }
            
            return ResponseEntity.ok(s_response);
        } catch (Exception e) {
            Map<String, Object> s_error = new HashMap<>();
            s_error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(s_error);
        }
    }

    /**
     * PUT: API - Обновить профиль
     */
    @PutMapping("/api/update")
    @ResponseBody
    public ResponseEntity<?> updateProfile(@RequestBody Map<String, Object> t_data, Authentication t_auth) {
        try {
            String s_currentEmail = t_auth.getName();
            User s_currentUser = m_userService.findByEmail(s_currentEmail);
            boolean s_isAdmin = t_auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
            
            Long s_targetUserId = t_data.get("userId") != null 
                ? Long.valueOf(t_data.get("userId").toString()) 
                : s_currentUser.getId();
            
            // Проверка прав: только админ может редактировать чужие профили
            if (!s_isAdmin && !s_targetUserId.equals(s_currentUser.getId())) {
                return ResponseEntity.status(403).body(Map.of("error", "Недостаточно прав"));
            }
            
            // Обновляем профиль только если админ
            if (!s_isAdmin) {
                return ResponseEntity.status(403).body(Map.of("error", "Только администратор может редактировать профиль"));
            }
            
            User s_targetUser = m_userService.getUserById(s_targetUserId);
            
            // Обновляем username если передан
            if (t_data.containsKey("username") && t_data.get("username") != null) {
                s_targetUser.setUsername(t_data.get("username").toString());
            }
            
            // Обновляем email если передан
            if (t_data.containsKey("email") && t_data.get("email") != null) {
                String s_newEmail = t_data.get("email").toString();
                // Проверяем, не занят ли email другим пользователем
                User s_existing = m_userService.findByEmail(s_newEmail);
                if (s_existing != null && !s_existing.getId().equals(s_targetUserId)) {
                    return ResponseEntity.badRequest().body(Map.of("error", "Email уже используется"));
                }
                s_targetUser.setEmail(s_newEmail);
            }
            
            m_userService.saveUser(s_targetUser);
            
            return ResponseEntity.ok(Map.of("message", "Профиль обновлен"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
