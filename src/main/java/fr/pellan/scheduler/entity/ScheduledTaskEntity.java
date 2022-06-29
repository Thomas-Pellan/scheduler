package fr.pellan.scheduler.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity used to store data about the scheduled task and it's parameters.
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema= "scheduled", name="scheduled_task")
public class ScheduledTaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="name")
    private String name;

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

    @OneToMany(mappedBy="scheduledTask", fetch = FetchType.EAGER)
    private List<ScheduledTaskInputEntity> inputs;
}
