package ru.danila.NauJava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.danila.NauJava.dao.EmployeeRepository;
import ru.danila.NauJava.dao.ReportRepository;
import ru.danila.NauJava.dao.UserRepository;
import ru.danila.NauJava.entity.Employee;
import ru.danila.NauJava.entity.Report;
import ru.danila.NauJava.entity.ReportStatus;
import ru.danila.NauJava.entity.User;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public ReportServiceImpl(ReportRepository reportRepository,
                             UserRepository userRepository,
                             EmployeeRepository employeeRepository) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Report getReportById(Long id) {
        return reportRepository.findById(id);
    }

    @Override
    public Long createReport() {
        Report report = new Report();
        report.setStatus(ReportStatus.CREATED);
        report.setContent("");
        Report savedReport = reportRepository.save(report);
        return savedReport.getId();
    }

    @Async
    @Override
    public CompletableFuture<Void> generateReportAsync(Long reportId) {
        System.out.println("Начало асинхронного формирования отчета " + reportId);

        try {
            // Общее время начала формирования отчета
            long reportStartTime = System.currentTimeMillis();

            // Результаты вычислений
            final int[] userCount = {0};
            final long[] userCountTime = {0};
            final List<Employee>[] employees = new List[]{null};
            final long[] employeeListTime = {0};

            // Поток 1: Подсчет количества пользователей
            Thread userCountThread = new Thread(() -> {
                long startTime = System.currentTimeMillis();
                try {
                    System.out.println("Поток 1: Подсчет пользователей запущен");
                    // Имитация долгой операции
                    Thread.sleep(2000);
                    // Реальное получение данных
                    userCount[0] = userRepository.findAll().size();
                    userCountTime[0] = System.currentTimeMillis() - startTime;
                    System.out.println("Поток 1: Найдено " + userCount[0] + " пользователей за " + userCountTime[0] + "ms");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Поток подсчета пользователей прерван", e);
                }
            });

            // Поток 2: Получение списка сотрудников
            Thread employeeListThread = new Thread(() -> {
                long startTime = System.currentTimeMillis();
                try {
                    System.out.println("Поток 2: Получение сотрудников запущен");
                    // Имитация долгой операции
                    Thread.sleep(3000);
                    // Реальное получение данных
                    employees[0] = employeeRepository.findAll();
                    employeeListTime[0] = System.currentTimeMillis() - startTime;
                    System.out.println("Поток 2: Найдено " + employees[0].size() + " сотрудников за " + employeeListTime[0] + "ms");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Поток получения сотрудников прерван", e);
                }
            });

            // Запускаем оба потока
            userCountThread.start();
            employeeListThread.start();

            // Ожидаем завершения обоих потоков
            userCountThread.join();
            employeeListThread.join();

            // Общее время формирования отчета
            long totalTime = System.currentTimeMillis() - reportStartTime;

            // Генерируем HTML отчет
            String htmlContent = generateHtmlReport(userCount[0], userCountTime[0], employees[0], employeeListTime[0], totalTime);

            // Обновляем отчет
            Report report = reportRepository.findById(reportId);
            if (report != null) {
                report.setContent(htmlContent);
                report.setStatus(ReportStatus.COMPLETED);
                reportRepository.update(report);
                System.out.println("Отчет " + reportId + " успешно сформирован за " + totalTime + "ms");
            } else {
                System.err.println("Отчет " + reportId + " не найден для обновления");
            }

        } catch (Exception e) {
            System.err.println("Ошибка при формировании отчета " + reportId + ": " + e.getMessage());
            e.printStackTrace();

            Report report = reportRepository.findById(reportId);
            if (report != null) {
                report.setStatus(ReportStatus.ERROR);
                report.setContent("Ошибка при формировании отчета: " + e.getMessage());
                reportRepository.update(report);
            }
        }

        return CompletableFuture.completedFuture(null);
    }

    private String generateHtmlReport(int userCount, long userCountTime,
                                      List<Employee> employees, long employeeListTime,
                                      long totalTime) {
        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html>");
        html.append("<html lang='ru'>");
        html.append("<head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<title>Статистика приложения</title>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; margin: 20px; line-height: 1.6; }");
        html.append("h1 { color: #2c3e50; border-bottom: 2px solid #3498db; padding-bottom: 10px; }");
        html.append("h2 { color: #34495e; margin-top: 30px; }");
        html.append(".stat-card { background: #f8f9fa; border-left: 4px solid #3498db; padding: 15px; margin: 15px 0; }");
        html.append(".time-info { color: #7f8c8d; font-size: 0.9em; margin-top: 5px; }");
        html.append("table { width: 100%; border-collapse: collapse; margin: 20px 0; }");
        html.append("th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }");
        html.append("th { background-color: #3498db; color: white; }");
        html.append("tr:nth-child(even) { background-color: #f2f2f2; }");
        html.append(".summary { background: #e8f6f3; padding: 15px; border-radius: 5px; margin: 20px 0; }");
        html.append("</style>");
        html.append("</head>");
        html.append("<body>");

        html.append("<h1>Отчет статистики приложения</h1>");

        // Статистика пользователей
        html.append("<div class='stat-card'>");
        html.append("<h2>Пользователи системы</h2>");
        html.append("<p><strong>Количество зарегистрированных пользователей: ").append(userCount).append("</strong></p>");
        html.append("<p class='time-info'>Время вычисления: ").append(userCountTime).append(" мс</p>");
        html.append("</div>");

        // Список сотрудников
        html.append("<div class='stat-card'>");
        html.append("<h2>Список сотрудников</h2>");

        if (employees.isEmpty()) {
            html.append("<p>Нет данных о сотрудниках</p>");
        } else {
            html.append("<table>");
            html.append("<thead><tr>");
            html.append("<th>ID</th>");
            html.append("<th>Имя</th>");
            html.append("<th>Фамилия</th>");
            html.append("<th>Отдел</th>");
            html.append("<th>Должность</th>");
            html.append("</tr></thead>");
            html.append("<tbody>");

            for (Employee employee : employees) {
                html.append("<tr>");
                html.append("<td>").append(employee.getId()).append("</td>");
                html.append("<td>").append(employee.getFirstName()).append("</td>");
                html.append("<td>").append(employee.getLastName()).append("</td>");
                html.append("<td>").append(employee.getDepartment()).append("</td>");
                html.append("<td>").append(employee.getPosition()).append("</td>");
                html.append("</tr>");
            }

            html.append("</tbody>");
            html.append("</table>");
            html.append("<p class='time-info'>Время вычисления: ").append(employeeListTime).append(" мс</p>");
            html.append("<p><strong>Всего сотрудников: ").append(employees.size()).append("</strong></p>");
        }
        html.append("</div>");

        // Итоговая статистика
        html.append("<div class='summary'>");
        html.append("<h2>Итоговая статистика</h2>");
        html.append("<p><strong>Общее время формирования отчета: ").append(totalTime).append(" мс</strong></p>");
        html.append("<p><em>Отчет сгенерирован: ").append(new java.util.Date()).append("</em></p>");
        html.append("</div>");

        html.append("</body>");
        html.append("</html>");

        return html.toString();
    }
}
