package fr.pellan.scheduler.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScheduledTaskInputDTO {

    private String key;

    private String value;
}
