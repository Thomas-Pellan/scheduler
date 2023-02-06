#!/bin/bash

if [ -z "$1" ];
then
  echo "Please specify 'dev' or 'prod' as arguments of the script"
  exit 0
fi

echo "Packaging the project into a jar file"
# Not running tests while packaging in prod (which you should of course do irl), this packaging should be done elsewhere ideally.
mvn clean package -DskipTests

#Getting maven package version of the jar generated
version=$(mvn -q -Dexec.executable="echo" -Dexec.args='${project.version}' --non-recursive exec:exec)
echo "version is : $version"

if [ $1 = "dev" ];
then

  if [ "$(docker ps -q -a --filter name=scheduler-dev)" ]; then
    echo "Stopping and removing previously used docker with name scheduler-dev"
    docker stop scheduler-dev && docker rm -f scheduler-dev
  fi

  echo "Starting dev stack"

  #Build docker image with the extracted mvn version
  docker build --build-arg PCK_VERSION="$version" --tag=docker-scheduler-api:"$version" .

  #Running docker in detached mode
  docker run \
    -e "SPRING_PROFILES_ACTIVE=dev" \
    -d \
    --name "scheduler-dev" \
    --network="host" \
    -p8080:8080 docker-scheduler-api:"$version"

  echo "Waiting for docker image to be ready ..."
  until [ "$(docker inspect --format='{{json .State.Status}}' scheduler-dev)"=="ready" ]; do
    echo "."
    sleep 1
  done

  exit 1
fi

if [ $1 = "prod" ];
then
  echo "Starting prod stack"

  if [ "$(docker ps -q -a --filter name=scheduler-app)" ]; then
    echo "Stopping and removing previously used docker with name scheduler-app"
    docker stop scheduler-app && docker rm -f scheduler-app
  fi

  if [ "$(docker ps -q -a --filter name=scheduler-adminer)" ]; then
      echo "Stopping and removing previously used docker with name scheduler-adminer"
      docker stop scheduler-adminer && docker rm -f scheduler-adminer
    fi

  if [ "$(docker ps -q -a --filter name=scheduler-db)" ]; then
    echo "Stopping and removing previously used docker with name scheduler-db"
    docker stop scheduler-db && docker rm -f scheduler-db
    sleep 2
    echo "Cleaning db volume"
    docker volume rm scheduler_scheduler-db
  fi

  echo "Executing docker compose"
  echo PCK_VERSION="$version" > .env
  docker-compose -f docker-compose-prod.yml --env-file .env up -d

  rm .env

  exit 2
fi