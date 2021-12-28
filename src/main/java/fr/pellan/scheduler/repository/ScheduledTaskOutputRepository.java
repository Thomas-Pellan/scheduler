package fr.pellan.scheduler.repository;

import fr.pellan.scheduler.entity.ScheduledTaskOutputEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ScheduledTaskOutputRepository extends CrudRepository<ScheduledTaskOutputEntity, Integer> {

}
