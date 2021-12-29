package fr.pellan.scheduler.repository;

import fr.pellan.scheduler.entity.ScheduledTaskEntity;
import fr.pellan.scheduler.entity.ScheduledTaskOutputEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ScheduledTaskOutputRepository extends CrudRepository<ScheduledTaskOutputEntity, Integer> {

    @Query("select o from ScheduledTaskOutputEntity o where o.scheduledTask.name = :name")
    List<ScheduledTaskOutputEntity> findByTaskName(String name);

    @Query("select o from ScheduledTaskOutputEntity o where o.scheduledTask = :scheduledTask")
    List<ScheduledTaskOutputEntity> findByTask(ScheduledTaskEntity scheduledTask);
}
