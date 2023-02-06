package fr.pellan.scheduler.task;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class TaskResultResponse implements Serializable  {

    private String data;

    private Boolean success;

    private String error;
}
