package fr.pellan.scheduler.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
public class ScheduledTaskOutputDTO implements Serializable {

    private LocalDateTime execution_date;

    private String data;

    private String state;

    private String log;
}
