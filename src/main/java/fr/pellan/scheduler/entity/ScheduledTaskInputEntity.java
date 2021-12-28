package fr.pellan.scheduler.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(schema= "scheduled", name="scheduled_task_input")
public class ScheduledTaskInputEntity {

    @Id
    @Column(name="id")
    private int id;

    @ManyToOne
    @JoinColumn(name="id_scheduled_task")
    private ScheduledTaskEntity scheduledTask;

    @Column(name="property_key")
    private String key;

    @Column(name="property_value")
    private String value;
}
