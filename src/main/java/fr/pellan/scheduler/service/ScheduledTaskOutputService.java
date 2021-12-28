package fr.pellan.scheduler.service;

import fr.pellan.scheduler.entity.ScheduledTaskEntity;
import fr.pellan.scheduler.entity.ScheduledTaskInputEntity;
import fr.pellan.scheduler.entity.ScheduledTaskOutputEntity;
import fr.pellan.scheduler.repository.ScheduledTaskOutputRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
public class ScheduledTaskOutputService {

    @Autowired
    private ScheduledTaskOutputRepository scheduledTaskOutputRepository;

    @Transactional
    public boolean delete(ScheduledTaskEntity task){

        List<ScheduledTaskOutputEntity> inputs = scheduledTaskOutputRepository.findByTask(task);
        if(!CollectionUtils.isEmpty(inputs)){
            scheduledTaskOutputRepository.deleteAll();
        }

        return true;
    }
}
