package fr.pellan.scheduler.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Dto containing data on the scheduled task
 * @see fr.pellan.scheduler.entity.ScheduledTaskEntity
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduledTaskDTO implements Serializable {

    private Integer id;

    private String name;

    private String cronExpression;

    private boolean active;

    private String url;

    private LocalDateTime lastExecution;

    private String lastResult;

    private List<ScheduledTaskInputDTO> inputs;
}
