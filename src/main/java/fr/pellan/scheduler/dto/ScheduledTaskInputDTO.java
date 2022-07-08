package fr.pellan.scheduler.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Dto used for shceduled task input data.
 * @see fr.pellan.scheduler.entity.ScheduledTaskInputEntity
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduledTaskInputDTO implements Serializable {

    private String key;

    private String value;
}
