FROM openjdk:17-jdk

ARG PCK_VERSION
COPY target/scheduler-${PCK_VERSION}.jar scheduler.jar
ENTRYPOINT ["java","-jar","/scheduler.jar"]

HEALTHCHECK --interval=10m --timeout=10s --retries=5 CMD wget http://localhost:8080/actuator/health | grep UP

