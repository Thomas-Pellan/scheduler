CREATE SCHEMA scheduler;

CREATE TABLE scheduler.cron_expression
(
    id INT NOT NULL AUTO_INCREMENT,
    cron_pattern VARCHAR(20) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE scheduler.scheduled_task
(
    id             INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    id_cron_expression INT NOT NULL,
    active         BOOLEAN     DEFAULT false,
    url            VARCHAR(100) NOT NULL,
    last_execution DATETIME       DEFAULT NULL,
    last_result    VARCHAR(20) DEFAULT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (id_cron_expression) REFERENCES scheduler.cron_expression (id)
);

CREATE TABLE scheduler.scheduled_task_input
(
    id             INT NOT NULL AUTO_INCREMENT,
    id_scheduled_task INT NOT NULL,
    property_key   VARCHAR(50) NOT NULL,
    property_value VARCHAR(50) DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE (id_scheduled_task, property_key, property_value),
    FOREIGN KEY (id_scheduled_task) REFERENCES scheduler.scheduled_task (id)
);

CREATE TABLE scheduler.scheduled_task_output
(
    id             INT NOT NULL AUTO_INCREMENT,
    id_scheduled_task INT NOT NULL,
    execution_date DATETIME NOT NULL,
    data   VARCHAR(500) DEFAULT NULL,
    state   VARCHAR(20) DEFAULT NULL,
    log   VARCHAR(500) DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE (id_scheduled_task, execution_date, state),
    FOREIGN KEY (id_scheduled_task) REFERENCES scheduler.scheduled_task (id)
);
