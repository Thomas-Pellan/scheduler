package fr.pellan.scheduler.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class ScheduledTaskInputDTO implements Serializable {

    private String key;

    private String value;
}
