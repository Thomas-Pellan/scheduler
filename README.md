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

### Doc - Tests

Documentation visinle once the app is started under http://localhost:8080/swagger-ui/index.html