package fr.pellan.scheduler.factory;

import fr.pellan.scheduler.entity.CronExpressionEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CronExceptionEntityFactoryTest {

    @InjectMocks
    CronExceptionEntityFactory cronExceptionEntityFactory;

    @Test
    void givenNullExpression_returnNullObject(){

        assertNull(cronExceptionEntityFactory.buildCronExpressionEntity(null));
    }

    @Test
    void givenInvalidExpression_returnNullObject(){

        assertNull(cronExceptionEntityFactory.buildCronExpressionEntity("* * * * * */d"));
    }

    @Test
    void givenValidExpression_returnNewObject(){

        String validExpression = "* * * * * *";
        CronExpressionEntity dummy = new CronExpressionEntity(null, validExpression);

        assertEquals(dummy, cronExceptionEntityFactory.buildCronExpressionEntity(validExpression));
    }
}
