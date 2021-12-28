package fr.pellan.scheduler.repository;

import fr.pellan.scheduler.entity.CronExpressionEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CronExpressionRepository extends CrudRepository<CronExpressionEntity, Integer> {

    @Query("select c from CronExpressionEntity c where c.cronPattern = :expression")
    CronExpressionEntity findByExpression(String expression);
}
