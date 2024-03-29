# Scheduler projet

### Goals

- Provide a way to call various tasks hosted in separate servers
- Handle responses and provide data about the execution of these tasks
- Expose data using rest api controller, for frontend use

### Features - Versions

- 0.0.1 : Spring/Maven project setup + entities and sql init
- 0.0.2 : creating cron tasks at startup, fire a simple http post to a target using the given configuration
- 0.0.3 : exposing mock controller for testing purpose and output testing
- 0.0.4 : adding controller for task editing, adding controller to reload thread pool manually
- 0.1.0 : cleaning and adding full test coverage with spring test and mockito, creating profiles to hide openapi ui

## Installation

### 'dev' profile 

- You should have a mysql 8 running locally with the sql under the ressource file executed on it and the required user created (see the application-dev property file)
- You should have docker installed and available for the current user
run command :
```
./startup.sh dev
```

This should package and create a docker running the spring app, once started you should see the documentation at the url : http://localhost:8080/swagger-ui/index.html

### 'prod' profile

- You should have docker installed and available for the current user
- You should have the docker compose plugin installed and available for the current user
  run command :
```
./startup.sh prod
```

This will run the docker compose to create a mysql image and a spring image working together.

### Doc - Tests

Documentation visible once the app is started under http://localhost:8080/swagger-ui/index.html (only for dev profile)