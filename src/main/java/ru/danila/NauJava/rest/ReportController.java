package ru.danila.NauJava.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.danila.NauJava.entity.Report;
import ru.danila.NauJava.entity.ReportStatus;
import ru.danila.NauJava.service.ReportService;

import java.util.HashMap;
import java.util.Map;

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
            // Создаем отчет
            Long reportId = reportService.createReport("Отчет сотрудников", 1L);

            // Запускаем асинхронное формирование отчета (НЕ ждем завершения)
            reportService.generateEmployeeReportAsync(reportId);

            Map<String, Object> response = new HashMap<>();
            response.put("reportId", reportId);
            response.put("status", "created");
            response.put("message", "Формирование отчета запущено");
            response.put("checkUrl", "/api/report/" + reportId);

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
            Report report = reportService.getReportById(id);

            if (report == null) {
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
     * GET: Получить список всех отчётов
     * URL: GET /api/reports
     */
    @GetMapping("/reports")
    public ResponseEntity<?> getAllReports() {
        try {
            var reports = reportService.getAllReports().stream()
                .map(report -> {
                    Map<String, Object> r = new HashMap<>();
                    r.put("id", report.getId());
                    r.put("title", report.getTitle());
                    r.put("status", report.getStatus().name());
                    r.put("createdAt", report.getCreatedDate());
                    r.put("content", report.getContent());
                    return r;
                })
                .toList();
            return ResponseEntity.ok(reports);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Не удалось получить список отчётов");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * POST: Создание отчета (альтернативный endpoint)
     * URL: POST /api/reports
     */
    @PostMapping("/reports")
    public ResponseEntity<Map<String, Object>> createReport(@RequestBody(required = false) Map<String, String> body) {
        try {
            String title = body != null && body.containsKey("title") ? body.get("title") : "Отчет сотрудников";
            Long reportId = reportService.createReport(title, 1L);
            reportService.generateEmployeeReportAsync(reportId);

            Map<String, Object> response = new HashMap<>();
            response.put("reportId", reportId);
            response.put("status", "created");
            response.put("message", "Формирование отчета запущено");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Не удалось создать отчет");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * DELETE: Удалить отчёт
     * URL: DELETE /api/reports/{id}
     */
    @DeleteMapping("/reports/{id}")
    public ResponseEntity<?> deleteReport(@PathVariable Long id) {
        try {
            reportService.deleteReport(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Не удалось удалить отчёт");
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
