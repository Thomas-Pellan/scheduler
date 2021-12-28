package fr.pellan.scheduler.repository;

import fr.pellan.scheduler.entity.ScheduledTaskInputEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ScheduledTaskInputRepository extends CrudRepository<ScheduledTaskInputEntity, Integer> {

}
