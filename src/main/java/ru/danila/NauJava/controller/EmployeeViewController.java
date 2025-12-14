package ru.danila.NauJava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.danila.NauJava.service.EmployeeService;

/**
 * WEB контроллер для HTML страниц
 */
@Controller
@RequestMapping("/employees")
public class EmployeeViewController {

    private final EmployeeService m_employeeService;

    @Autowired
    public EmployeeViewController(EmployeeService t_employeeService) {
        this.m_employeeService = t_employeeService;
    }

    /**
     * GET: Страница со списком всех сотрудников (корневой путь /employees)
     */
    @GetMapping({"", "/"})
    public String getAllEmployeesRoot(Model t_model) {
        return getAllEmployees(t_model);
    }

    /**
     * GET: Страница со списком всех сотрудников
     */
    @GetMapping("/list")
    public String getAllEmployees(Model t_model) {
        t_model.addAttribute("employees", m_employeeService.getAllEmployees());
        return "employeeList";
    }
}