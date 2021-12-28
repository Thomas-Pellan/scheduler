package fr.pellan.scheduler.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(schema= "scheduled", name="scheduled_task_output")
public class ScheduledTaskOutputEntity {

    @Id
    @Column(name="id")
    private int id;

    @ManyToOne
    @JoinColumn(name="id_scheduled_task")
    private ScheduledTaskEntity scheduledTask;

    @Column(name="execution_date")
    private LocalDateTime executionDate;

    @Column(name="return_value")
    private String returnValue;
}
