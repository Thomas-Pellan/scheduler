INSERT INTO scheduler.cron_expression(cron_pattern)
VALUES (
    '*/5 * * * * *'
);


INSERT INTO scheduler.scheduled_task(id_cron_expression, name, active, url)
VALUES (
   1,
   'testScheduledTask1',
   true,
   'http://localhost:8099/helloworld/test'
);

INSERT INTO scheduler.scheduled_task_input(id_scheduled_task, property_key, property_value)
VALUES (
   1,
   'testData',
   '1'
);