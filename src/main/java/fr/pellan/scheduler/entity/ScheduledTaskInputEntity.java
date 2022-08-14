package fr.pellan.scheduler.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Entity used to store input data for a scheduled task.
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema= "scheduled", name="scheduled_task_input")
public class ScheduledTaskInputEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_scheduled_task", nullable = false)
    private ScheduledTaskEntity scheduledTask;

    @Column(name="property_key")
    private String key;

    @Column(name="property_value")
    private String value;
}
