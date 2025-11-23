package ru.danila.NauJava.entity;

/**
 * Сущность для хранения отчетов
 */
public class Report {
    private Long id;
    private ReportStatus status;
    private String content;

    public Report() {
    }

    public Report(Long id) {
        this.id = id;
        this.status = ReportStatus.CREATED;
        this.content = "";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
