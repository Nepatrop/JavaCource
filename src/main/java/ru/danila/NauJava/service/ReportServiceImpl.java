package ru.danila.NauJava.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.danila.NauJava.entity.Department;
import ru.danila.NauJava.entity.Employee;
import ru.danila.NauJava.entity.Report;
import ru.danila.NauJava.entity.ReportStatus;
import ru.danila.NauJava.entity.User;
import ru.danila.NauJava.exception.ResourceNotFoundException;
import ru.danila.NauJava.repository.DepartmentRepository;
import ru.danila.NauJava.repository.EmployeeRepository;
import ru.danila.NauJava.repository.ReportRepository;
import ru.danila.NauJava.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Реализация сервиса для работы с отчетами
 * Управляет созданием, генерацией и получением отчетов
 */
@Slf4j
@Service
@Transactional
public class ReportServiceImpl implements ReportService {

    private final ReportRepository m_reportRepository;
    private final UserRepository m_userRepository;
    private final EmployeeRepository m_employeeRepository;
    private final DepartmentRepository m_departmentRepository;

    /**
     * Конструктор с внедрением зависимостей
     */
    @Autowired
    public ReportServiceImpl(ReportRepository t_reportRepository,
                            UserRepository t_userRepository,
                            EmployeeRepository t_employeeRepository,
                            DepartmentRepository t_departmentRepository) {
        this.m_reportRepository = t_reportRepository;
        this.m_userRepository = t_userRepository;
        this.m_employeeRepository = t_employeeRepository;
        this.m_departmentRepository = t_departmentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Report getReportById(Long t_id) {
        return m_reportRepository.findById(t_id)
                .orElseThrow(() -> new ResourceNotFoundException("Отчет с ID " + t_id + " не найден"));
    }

    @Override
    public Long createReport(String t_title, Long t_userId) {
        
        User s_user = m_userRepository.findById(t_userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));

        Report s_report = new Report();
        s_report.setM_title(t_title);
        s_report.setM_status(ReportStatus.CREATED);
        s_report.setM_content("");
        s_report.setM_createdBy(s_user);

        Report s_savedReport = m_reportRepository.save(s_report);
        return s_savedReport.getM_id();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Report> getAllReports() {
        return m_reportRepository.findAllOrderByCreatedDateDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Report> getReportsByStatus(ReportStatus t_status) {
        return m_reportRepository.findByM_status(t_status);
    }

    @Override
    public void deleteReport(Long t_id) {
        Report s_report = getReportById(t_id);
        m_reportRepository.delete(s_report);
    }

    @Override
    @Async
    public CompletableFuture<Void> generateEmployeeReportAsync(Long t_reportId) {
        try {
            long s_startTime = System.currentTimeMillis();

            Report s_report = getReportById(t_reportId);
            
            List<Employee> s_employees = m_employeeRepository.findAllActive();
            List<Department> s_departments = m_departmentRepository.findAll();
            
            String s_htmlContent = generateEmployeeHtmlReport(s_employees, s_departments);

            s_report.setM_content(s_htmlContent);
            s_report.setM_status(ReportStatus.COMPLETED);
            s_report.setM_completedDate(LocalDateTime.now());
            
            m_reportRepository.save(s_report);
        } catch (Exception e) {
            try {
                Report s_report = getReportById(t_reportId);
                s_report.setM_status(ReportStatus.ERROR);
                s_report.setM_content("Ошибка при формировании отчета: " + e.getMessage());
                m_reportRepository.save(s_report);
            } catch (Exception ex) {
                log.error("Не удалось обновить статус отчета на ERROR", ex);
            }
        }

        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Async
    public CompletableFuture<Void> generateDepartmentReportAsync(Long t_reportId) {
        try {
            long s_startTime = System.currentTimeMillis();

            Report s_report = getReportById(t_reportId);
            
            List<Department> s_departments = m_departmentRepository.findAll();
            
            String s_htmlContent = generateDepartmentHtmlReport(s_departments);

            s_report.setM_content(s_htmlContent);
            s_report.setM_status(ReportStatus.COMPLETED);
            s_report.setM_completedDate(LocalDateTime.now());
            
            m_reportRepository.save(s_report);
        } catch (Exception e) {
            try {
                Report s_report = getReportById(t_reportId);
                s_report.setM_status(ReportStatus.ERROR);
                s_report.setM_content("Ошибка при формировании отчета: " + e.getMessage());
                m_reportRepository.save(s_report);
            } catch (Exception ex) {
                log.error("Не удалось обновить статус отчета на ERROR", ex);
            }
        }

        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Report> getReportsByCreatedDate(LocalDateTime t_startDate, LocalDateTime t_endDate) {
        return m_reportRepository.findByCreatedDateBetween(t_startDate, t_endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Report> getUserReports(Long t_userId) {
        User s_user = m_userRepository.findById(t_userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));

        return m_reportRepository.findByM_createdBy(s_user);
    }

    /**
     * Генерирует HTML отчет по сотрудникам
     */
    private String generateEmployeeHtmlReport(List<Employee> t_employees, List<Department> t_departments) {
        StringBuilder s_html = new StringBuilder();
        
        s_html.append("<!DOCTYPE html>");
        s_html.append("<html lang='ru'>");
        s_html.append("<head>");
        s_html.append("<meta charset='UTF-8'>");
        s_html.append("<title>Отчет по сотрудникам</title>");
        s_html.append("<style>");
        s_html.append("body { font-family: Arial, sans-serif; margin: 20px; line-height: 1.6; }");
        s_html.append("h1 { color: #2c3e50; border-bottom: 2px solid #3498db; padding-bottom: 10px; }");
        s_html.append("h2 { color: #34495e; margin-top: 30px; }");
        s_html.append(".stat-card { background: #f8f9fa; border-left: 4px solid #3498db; padding: 15px; margin: 15px 0; }");
        s_html.append("table { width: 100%; border-collapse: collapse; margin: 20px 0; }");
        s_html.append("th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }");
        s_html.append("th { background-color: #3498db; color: white; }");
        s_html.append("tr:nth-child(even) { background-color: #f2f2f2; }");
        s_html.append(".summary { background: #e8f6f3; padding: 15px; border-radius: 5px; margin: 20px 0; }");
        s_html.append("</style>");
        s_html.append("</head>");
        s_html.append("<body>");

        s_html.append("<h1>Отчет по сотрудникам компании</h1>");

        // Статистика
        s_html.append("<div class='stat-card'>");
        s_html.append("<h2>Статистика</h2>");
        s_html.append("<p><strong>Всего сотрудников: ").append(t_employees.size()).append("</strong></p>");
        s_html.append("<p><strong>Всего отделов: ").append(t_departments.size()).append("</strong></p>");
        s_html.append("</div>");

        // Таблица сотрудников
        s_html.append("<div class='stat-card'>");
        s_html.append("<h2>Список сотрудников</h2>");

        if (t_employees.isEmpty()) {
            s_html.append("<p>Нет данных о сотрудниках</p>");
        } else {
            s_html.append("<table>");
            s_html.append("<thead><tr>");
            s_html.append("<th>ID</th>");
            s_html.append("<th>Имя</th>");
            s_html.append("<th>Фамилия</th>");
            s_html.append("<th>Email</th>");
            s_html.append("<th>Должность</th>");
            s_html.append("<th>Отдел</th>");
            s_html.append("</tr></thead>");
            s_html.append("<tbody>");

            for (Employee s_employee : t_employees) {
                String s_deptName = s_employee.getM_department() != null ? 
                    s_employee.getM_department().getM_name() : "N/A";
                    
                s_html.append("<tr>");
                s_html.append("<td>").append(s_employee.getM_id()).append("</td>");
                s_html.append("<td>").append(s_employee.getM_firstName()).append("</td>");
                s_html.append("<td>").append(s_employee.getM_lastName()).append("</td>");
                s_html.append("<td>").append(s_employee.getM_email()).append("</td>");
                s_html.append("<td>").append(s_employee.getM_position()).append("</td>");
                s_html.append("<td>").append(s_deptName).append("</td>");
                s_html.append("</tr>");
            }

            s_html.append("</tbody>");
            s_html.append("</table>");
        }
        s_html.append("</div>");

        // Итоги
        s_html.append("<div class='summary'>");
        s_html.append("<h2>Итоговая информация</h2>");
        s_html.append("<p><em>Отчет сгенерирован: ").append(LocalDateTime.now()).append("</em></p>");
        s_html.append("</div>");

        s_html.append("</body>");
        s_html.append("</html>");

        return s_html.toString();
    }

    /**
     * Генерирует HTML отчет по отделам
     */
    private String generateDepartmentHtmlReport(List<Department> t_departments) {
        StringBuilder s_html = new StringBuilder();
        
        s_html.append("<!DOCTYPE html>");
        s_html.append("<html lang='ru'>");
        s_html.append("<head>");
        s_html.append("<meta charset='UTF-8'>");
        s_html.append("<title>Отчет по отделам</title>");
        s_html.append("<style>");
        s_html.append("body { font-family: Arial, sans-serif; margin: 20px; line-height: 1.6; }");
        s_html.append("h1 { color: #2c3e50; border-bottom: 2px solid #3498db; padding-bottom: 10px; }");
        s_html.append("h2 { color: #34495e; margin-top: 30px; }");
        s_html.append(".stat-card { background: #f8f9fa; border-left: 4px solid #3498db; padding: 15px; margin: 15px 0; }");
        s_html.append("table { width: 100%; border-collapse: collapse; margin: 20px 0; }");
        s_html.append("th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }");
        s_html.append("th { background-color: #3498db; color: white; }");
        s_html.append("tr:nth-child(even) { background-color: #f2f2f2; }");
        s_html.append(".summary { background: #e8f6f3; padding: 15px; border-radius: 5px; margin: 20px 0; }");
        s_html.append("</style>");
        s_html.append("</head>");
        s_html.append("<body>");

        s_html.append("<h1>Отчет по отделам компании</h1>");

        // Статистика
        s_html.append("<div class='stat-card'>");
        s_html.append("<h2>Статистика</h2>");
        s_html.append("<p><strong>Всего отделов: ").append(t_departments.size()).append("</strong></p>");
        s_html.append("</div>");

        // Таблица отделов
        s_html.append("<div class='stat-card'>");
        s_html.append("<h2>Структура отделов</h2>");

        if (t_departments.isEmpty()) {
            s_html.append("<p>Нет данных об отделах</p>");
        } else {
            s_html.append("<table>");
            s_html.append("<thead><tr>");
            s_html.append("<th>ID</th>");
            s_html.append("<th>Название</th>");
            s_html.append("<th>Описание</th>");
            s_html.append("<th>Количество сотрудников</th>");
            s_html.append("<th>Руководитель</th>");
            s_html.append("</tr></thead>");
            s_html.append("<tbody>");

            for (Department s_dept : t_departments) {
                int s_empCount = s_dept.getM_employees() != null ? s_dept.getM_employees().size() : 0;
                String s_headName = s_dept.getM_head() != null ? 
                    s_dept.getM_head().getFullName() : "Не назначен";
                    
                s_html.append("<tr>");
                s_html.append("<td>").append(s_dept.getM_id()).append("</td>");
                s_html.append("<td>").append(s_dept.getM_name()).append("</td>");
                s_html.append("<td>").append(s_dept.getM_description() != null ? s_dept.getM_description() : "-").append("</td>");
                s_html.append("<td>").append(s_empCount).append("</td>");
                s_html.append("<td>").append(s_headName).append("</td>");
                s_html.append("</tr>");
            }

            s_html.append("</tbody>");
            s_html.append("</table>");
        }
        s_html.append("</div>");

        // Итоги
        s_html.append("<div class='summary'>");
        s_html.append("<h2>Итоговая информация</h2>");
        s_html.append("<p><em>Отчет сгенерирован: ").append(LocalDateTime.now()).append("</em></p>");
        s_html.append("</div>");

        s_html.append("</body>");
        s_html.append("</html>");

        return s_html.toString();
    }
}
