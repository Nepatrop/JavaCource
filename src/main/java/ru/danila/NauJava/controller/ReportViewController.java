package ru.danila.NauJava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.danila.NauJava.service.ReportService;

/**
 * WEB контроллер для HTML страниц отчётов
 */
@Controller
@RequestMapping("/reports")
public class ReportViewController {

    private final ReportService m_reportService;

    @Autowired
    public ReportViewController(ReportService t_reportService) {
        this.m_reportService = t_reportService;
    }

    /**
     * GET: Страница со списком всех отчётов
     */
    @GetMapping({"", "/", "/list"})
    public String getAllReports(Model t_model) {
        t_model.addAttribute("reports", m_reportService.getAllReports());
        return "reportList";
    }
}
