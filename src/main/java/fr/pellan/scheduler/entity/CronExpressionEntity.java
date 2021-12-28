package fr.pellan.scheduler.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(schema= "scheduled", name="cron_expression")
public class CronExpressionEntity {

    @Id
    @Column(name="id")
    private int id;

    @Column(name="cron_pattern")
    private String cronPattern;
}
