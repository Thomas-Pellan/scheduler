package fr.pellan.scheduler.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
