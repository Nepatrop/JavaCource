package ru.danila.NauJava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.danila.NauJava.service.DepartmentService;

/**
 * WEB контроллер для HTML страниц отделов
 */
@Controller
@RequestMapping("/departments")
public class DepartmentViewController {

    private final DepartmentService m_departmentService;

    @Autowired
    public DepartmentViewController(DepartmentService t_departmentService) {
        this.m_departmentService = t_departmentService;
    }

    /**
     * GET: Страница со списком всех отделов
     */
    @GetMapping({"", "/", "/list"})
    public String getAllDepartments(Model t_model) {
        t_model.addAttribute("departments", m_departmentService.getAllDepartments());
        return "departmentList";
    }
}
