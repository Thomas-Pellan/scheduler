package fr.pellan.scheduler.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Dto used to store data sent back by the shceudled tasks
 * @see fr.pellan.scheduler.entity.ScheduledTaskOutputEntity
 */
@Data
@Builder
@AllArgsConstructor
public class ScheduledTaskOutputDTO implements Serializable {

    private LocalDateTime executionDate;

    private String data;

    private String state;

    private String log;
}
