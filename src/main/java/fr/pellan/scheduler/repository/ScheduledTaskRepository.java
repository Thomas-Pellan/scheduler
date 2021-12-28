package fr.pellan.scheduler.repository;

import fr.pellan.scheduler.entity.ScheduledTaskEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ScheduledTaskRepository extends CrudRepository<ScheduledTaskEntity, Integer> {

    @Query("select s from ScheduledTaskEntity s where s.active = true")
    List<ScheduledTaskEntity> findActive();
}
