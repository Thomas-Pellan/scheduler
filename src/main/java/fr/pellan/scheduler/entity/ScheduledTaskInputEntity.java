package fr.pellan.scheduler.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(schema= "scheduled", name="scheduled_task_input")
public class ScheduledTaskInputEntity {

    @Id
    @Column(name="id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_scheduled_task", nullable = false)
    private ScheduledTaskEntity scheduledTask;

    @Column(name="property_key")
    private String key;

    @Column(name="property_value")
    private String value;
}
