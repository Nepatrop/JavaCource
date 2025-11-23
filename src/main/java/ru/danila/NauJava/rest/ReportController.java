package ru.danila.NauJava.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.danila.NauJava.entity.Report;
import ru.danila.NauJava.entity.ReportStatus;
import ru.danila.NauJava.service.ReportService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * REST контроллер для работы с отчетами
 */
@RestController
@RequestMapping("/api")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * POST: Создание и запуск формирования отчета
     * URL: POST /api/generate-report
     */
    @PostMapping("/generate-report")
    public ResponseEntity<Map<String, Object>> generateReport() {
        try {
            System.out.println("Получен запрос на создание отчета");

            // Создаем отчет
            Long reportId = reportService.createReport();
            System.out.println("Создан отчет с ID: " + reportId);

            // Запускаем асинхронное формирование отчета (НЕ ждем завершения)
            CompletableFuture<Void> future = reportService.generateReportAsync(reportId);

            Map<String, Object> response = new HashMap<>();
            response.put("reportId", reportId);
            response.put("status", "created");
            response.put("message", "Формирование отчета запущено");
            response.put("checkUrl", "/api/report/" + reportId);

            System.out.println("Асинхронное формирование отчета " + reportId + " запущено");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("Ошибка при создании отчета: " + e.getMessage());
            e.printStackTrace();

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Не удалось создать отчет");
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * GET: Получение отчета по ID
     * URL: GET /api/report/{id}
     */
    @GetMapping("/report/{id}")
    public ResponseEntity<Map<String, Object>> getReport(@PathVariable Long id) {
        try {
            System.out.println("Запрос отчета с ID: " + id);

            Report report = reportService.getReportById(id);

            if (report == null) {
                System.out.println("Отчет с ID " + id + " не найден");
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "Отчет не найден");
                errorResponse.put("reportId", id);
                return ResponseEntity.notFound().build();
            }

            Map<String, Object> response = new HashMap<>();
            response.put("reportId", report.getId());
            response.put("status", report.getStatus().getDisplayName());

            // В зависимости от статуса возвращаем разную информацию
            if (report.getStatus() == ReportStatus.COMPLETED) {
                response.put("content", report.getContent());
                response.put("message", "Отчет успешно сформирован");
            } else if (report.getStatus() == ReportStatus.CREATED) {
                response.put("message", "Отчет еще формируется...");
            } else if (report.getStatus() == ReportStatus.ERROR) {
                response.put("error", "При формировании отчета произошла ошибка");
                response.put("content", report.getContent());
            }

            System.out.println("Отчет " + id + " получен, статус: " + report.getStatus());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("Ошибка при получении отчета " + id + ": " + e.getMessage());
            e.printStackTrace();

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Не удалось получить отчет");
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * GET: Информация о API отчетов
     * URL: GET /api/reports-info
     */
    @GetMapping("/reports-info")
    public ResponseEntity<Map<String, Object>> getReportsInfo() {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "API для работы с отчетами");
            response.put("availableEndpoints", new String[] {
                    "POST /api/generate-report - создать и запустить формирование отчета",
                    "GET /api/report/{id} - получить отчет по ID",
                    "GET /api/reports-info - получить информацию об API"
            });
            response.put("note", "Для тестирования создайте отчет через POST /api/generate-report");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Не удалось получить информацию об отчетах");
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * GET: Прямой просмотр отчета в браузере как HTML
     * URL: GET /api/report/{id}/view
     */
    @GetMapping("/report/{id}/view")
    public String viewReport(@PathVariable Long id) {
        try {
            Report report = reportService.getReportById(id);

            if (report == null) {
                return "<h1>Отчет не найден</h1>";
            }

            if (report.getStatus() != ReportStatus.COMPLETED) {
                return "<h1>Отчет еще формируется или произошла ошибка</h1>";
            }

            return report.getContent();

        } catch (Exception e) {
            return "<h1>Ошибка при получении отчета: " + e.getMessage() + "</h1>";
        }
    }
}
