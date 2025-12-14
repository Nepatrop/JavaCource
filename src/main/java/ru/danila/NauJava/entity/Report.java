package ru.danila.NauJava.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Класс сущности "Отчет"
 * Содержит информацию об отчетах, сгенерированных в системе
 */
@Entity
@Table(name = "reports", indexes = {
        @Index(name = "idx_report_status", columnList = "status"),
        @Index(name = "idx_report_created_by", columnList = "created_by_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    
    /**
     * Уникальный идентификатор отчета
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long m_id = 0L;

    /**
     * Название отчета
     */
    @Column(name = "title", nullable = false, length = 200)
    private String m_title = "";

    /**
     * Статус формирования отчета
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReportStatus m_status = ReportStatus.CREATED;

    /**
     * HTML контент отчета
     */
    @Column(name = "content", columnDefinition = "TEXT")
    private String m_content = "";

    /**
     * Дата создания отчета
     */
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime m_createdDate = LocalDateTime.now();

    /**
     * Дата завершения отчета
     */
    @Column(name = "completed_date")
    private LocalDateTime m_completedDate;

    /**
     * Пользователь, создавший отчет
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id", nullable = false)
    private User m_createdBy;

    /**
     * Конструктор с основными параметрами
     */
    public Report(Long t_id) {
        this.m_id = t_id;
        this.m_status = ReportStatus.CREATED;
        this.m_content = "";
    }

    /**
     * Получить ID отчета
     */
    public Long getId() {
        return m_id;
    }

    /**
     * Установить ID отчета
     */
    public void setId(Long t_id) {
        this.m_id = t_id;
    }

    /**
     * Получить статус отчета
     */
    public ReportStatus getStatus() {
        return m_status;
    }

    /**
     * Установить статус отчета
     */
    public void setStatus(ReportStatus t_status) {
        this.m_status = t_status;
    }

    /**
     * Получить контент отчета
     */
    public String getContent() {
        return m_content;
    }

    /**
     * Установить контент отчета
     */
    public void setContent(String t_content) {
        this.m_content = t_content;
    }

    /**
     * Получить название отчета
     */
    public String getTitle() {
        return m_title;
    }

    /**
     * Установить название отчета
     */
    public void setTitle(String t_title) {
        this.m_title = t_title;
    }

    /**
     * Получить дату создания отчета
     */
    public LocalDateTime getCreatedDate() {
        return m_createdDate;
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + m_id +
                ", title='" + m_title + '\'' +
                ", status=" + m_status +
                ", createdDate=" + m_createdDate +
                ", completedDate=" + m_completedDate +
                '}';
    }
}
