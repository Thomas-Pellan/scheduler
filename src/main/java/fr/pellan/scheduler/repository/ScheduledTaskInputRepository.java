package fr.pellan.scheduler.repository;

import fr.pellan.scheduler.entity.ScheduledTaskEntity;
import fr.pellan.scheduler.entity.ScheduledTaskInputEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ScheduledTaskInputRepository extends CrudRepository<ScheduledTaskInputEntity, Integer> {

    @Query("select i from ScheduledTaskInputEntity i where i.scheduledTask = :scheduledTask")
    List<ScheduledTaskInputEntity> findByTask(ScheduledTaskEntity scheduledTask);
}
