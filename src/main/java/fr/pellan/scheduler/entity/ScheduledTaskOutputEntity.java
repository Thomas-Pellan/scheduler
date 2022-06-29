package fr.pellan.scheduler.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity used to store data on a scheduled task output and execution
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(schema= "scheduled", name="scheduled_task_output")
public class ScheduledTaskOutputEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name="id_scheduled_task")
    private ScheduledTaskEntity scheduledTask;

    @Column(name="execution_date")
    private LocalDateTime executionDate;

    @Column(name="data")
    private String data;

    @Column(name="state")
    private String state;

    @Column(name="log")
    private String log;
}
