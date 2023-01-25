package fr.pellan.scheduler.service;

import fr.pellan.scheduler.entity.CronExpressionEntity;
import fr.pellan.scheduler.factory.CronExceptionEntityFactory;
import fr.pellan.scheduler.repository.CronExpressionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CronExpressionServiceTest {

    @InjectMocks
    CronExpressionService cronExpressionService;

    @Mock
    CronExpressionRepository cronExpressionRepository;

    @Mock
    CronExceptionEntityFactory cronExceptionEntityFactory;

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void givenValidCronExpression_returnValid(){

        String validCronExpression = "* * * * * *";
        assertTrue(cronExpressionService.validate(validCronExpression));
    }

    @Test
    void givenInValidCronExpression_returnInValid(){

        String validCronExpression = "* */12.5 * * *";
        assertFalse(cronExpressionService.validate(validCronExpression));
    }

    @Test
    void givenNewExpression_whenSaved_sendItBack(){

        String validCronExpression = "* */2 * * * *";

        when(cronExpressionRepository.save(Mockito.any(CronExpressionEntity.class))).then(new Answer<CronExpressionEntity>() {
            int sequence = 1;

            @Override
            public CronExpressionEntity answer(InvocationOnMock invocation) {
                CronExpressionEntity cron = invocation.getArgument(0);
                cron.setId(sequence++);
                return cron;
            }
        });
        when(cronExceptionEntityFactory.buildCronExpressionEntity(Mockito.any(String.class))).then((Answer<CronExpressionEntity>) invocation -> {
            CronExpressionEntity cron = new CronExpressionEntity(null, invocation.getArgument(0));
            return cron;
        });

        CronExpressionEntity expression = cronExpressionService.createExpression(validCronExpression);

        verify(cronExpressionRepository).save(expression);
        assertNotNull(expression);
        assertEquals(expression.getCronPattern(), validCronExpression);
        assertEquals(expression.getId(), 1);
    }

    @Test
    void givenExistingExpression_whenSaved_sendExistingBack(){

        String validCronExpression = "* */2 * * * *";
        CronExpressionEntity existingDummyExpression = new CronExpressionEntity(2, validCronExpression);

        when(cronExpressionRepository.findByExpression(Mockito.any(String.class))).then((Answer<CronExpressionEntity>) invocation -> existingDummyExpression);

        CronExpressionEntity expression = cronExpressionService.createExpression(validCronExpression);

        verify(cronExpressionRepository).findByExpression(validCronExpression);
        assertNotNull(expression);
        assertEquals(expression.getCronPattern(), existingDummyExpression.getCronPattern());
        assertEquals(expression.getId(), existingDummyExpression.getId());
    }
}
