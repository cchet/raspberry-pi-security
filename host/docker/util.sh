#!/bin/bash
# Please set DOCKER_INFRASTRUCTURE_HOME env variable

function start {
	su - docker -c "docker-compose -f  ${DOCKER_INFRASTRUCTURE_HOME}/docker-compose.yml up -d"
}

function stop {
	 su - docker -c "docker-compose -f  ${DOCKER_INFRASTRUCTURE_HOME}/docker-compose.yml stop"
}

function kill {
         su - docker -c "docker-compose -f  ${DOCKER_INFRASTRUCTURE_HOME}/docker-compose.yml kill"
}

function restart {
        stop && \
        start
}

function kill-restart {
	kill && \
	start
}

function recreate {
	su - docker -c "docker-compose -f  ${DOCKER_INFRASTRUCTURE_HOME}/docker-compose.yml down   &&  \
	                docker-compose -f  ${DOCKER_INFRASTRUCTURE_HOME}/docker-compose.yml build"
}

function recreate-start {
	recreate && \
	start
}

function kill-recreate {
        kill && \
	recreate
}

function kill-recreate-start {
	kill-recreate && \
	start
}

function cleanAll {
  cleanDandlingImages
  cleanContainers
}

function cleanDandlingImages {
  docker rmi $(docker images --filter "dangling=true" -q --no-trunc)
}

function cleanContainers {
  docker rm $(docker ps -qa --no-trunc --filter "status=exited")
}

case $1 in
  start|stop|kill|restart|kill-restart|recreate|recreate-start|kill-recreate|kill-recreate-start)
  if [ -n "${DOCKER_INFRASTRUCTURE_HOME}" ];
  then
    echo "'DOCKER_INFRASTRUCTURE_HOME' env variable must be set !!!"
    exit -1
  else
    "$1"
  fi;;
  cleanAll|cleanDandlingImages|cleanContainers)
    "$1"
  *)
  echo "Unkown command entered '$1'"
  exit -1;;
esac
