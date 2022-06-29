package fr.pellan.scheduler.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * Dto used for shceduled task input data.
 * @see fr.pellan.scheduler.entity.ScheduledTaskInputEntity
 */
@Data
@Builder
public class ScheduledTaskInputDTO implements Serializable {

    private String key;

    private String value;
}
