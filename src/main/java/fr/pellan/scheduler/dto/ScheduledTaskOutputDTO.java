package fr.pellan.scheduler.dto;

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
public class ScheduledTaskOutputDTO implements Serializable {

    private LocalDateTime execution_date;

    private String data;

    private String state;

    private String log;
}
