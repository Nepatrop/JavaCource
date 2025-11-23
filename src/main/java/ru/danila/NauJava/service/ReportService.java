package ru.danila.NauJava.service;

import ru.danila.NauJava.entity.Report;
import java.util.concurrent.CompletableFuture;

public interface ReportService {
    Report getReportById(Long id);
    Long createReport();
    CompletableFuture<Void> generateReportAsync(Long reportId);
}
