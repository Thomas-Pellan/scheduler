package fr.pellan.scheduler.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(schema= "scheduled", name="scheduled_task")
public class ScheduledTaskEntity {

    @Id
    @Column(name="id")
    private int id;

    @ManyToOne
    @JoinColumn(name="id_cron_expression")
    private CronExpressionEntity cronExpression;

    @Column(name="active")
    private boolean active;

    @Column(name="url")
    private String url;

    @Column(name="last_execution")
    private LocalDateTime lastExecution;

    @Column(name="last_result")
    private String lastResult;
}
