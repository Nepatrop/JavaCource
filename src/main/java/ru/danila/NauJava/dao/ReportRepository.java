package ru.danila.NauJava.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.danila.NauJava.entity.Report;
import ru.danila.NauJava.entity.ReportStatus;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Репозиторий для работы с отчетами
 */
@Repository
public class ReportRepository {

    private final List<Report> reportContainer;
    private final AtomicLong idCounter = new AtomicLong(1);

    @Autowired
    public ReportRepository(List<Report> reportContainer) {
        this.reportContainer = reportContainer;
    }

    public Report save(Report report) {
        if (report.getId() == null) {
            report.setId(idCounter.getAndIncrement());
        }
        reportContainer.add(report);
        return report;
    }

    public Report findById(Long id) {
        Optional<Report> foundReport = reportContainer.stream()
                .filter(report -> report.getId().equals(id))
                .findFirst();
        return foundReport.orElse(null);
    }

    public void update(Report report) {
        Report existing = findById(report.getId());
        if (existing != null) {
            existing.setStatus(report.getStatus());
            existing.setContent(report.getContent());
        }
    }

    public List<Report> findAll() {
        return reportContainer;
    }

    public List<Report> findByStatus(ReportStatus status) {
        return reportContainer.stream()
                .filter(report -> report.getStatus() == status)
                .toList();
    }
}
