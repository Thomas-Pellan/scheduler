UPDATE scheduler.scheduled_task
SET url = 'http://localhost:8080/test/helloworld'
WHERE name = 'testScheduledTask1';