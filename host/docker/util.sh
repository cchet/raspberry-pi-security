#!/bin/bash
# Please set DOCKER_INFRASTRUCTURE_HOME env variable

function start {
	 docker-compose -f  ${DOCKER_INFRASTRUCTURE_HOME}/docker-compose.yml up -d
}

function stop {
	 docker-compose -f  ${DOCKER_INFRASTRUCTURE_HOME}/docker-compose.yml stop
}

function kill {
         docker-compose -f  ${DOCKER_INFRASTRUCTURE_HOME}/docker-compose.yml kill
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
	docker-compose -f  ${DOCKER_INFRASTRUCTURE_HOME}/docker-compose.yml down   &&  \
	docker-compose -f  ${DOCKER_INFRASTRUCTURE_HOME}/docker-compose.yml build
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
  cleanDanglingImages
  cleanContainers
}

function cleanDanglingImages {
  docker rmi $(docker images --filter "dangling=true" -q --no-trunc)
}

function cleanContainers {
  docker rm $(docker ps -qa --no-trunc --filter "status=exited")
}

function rebuildRpiBase() {
	docker rmi rpisec-rpi-base
	docker build --file ${DOCKER_INFRASTRUCTURE_HOME}/base/Dockerfile --label rpisec-rpi-base
}

case $1 in
  start|stop|kill|restart|kill-restart|recreate|recreate-start|kill-recreate|kill-recreate-start|rebuildRpiBase)
  if [ -n "${DOCKER_INFRASTRUCTURE_HOME}" ];
  then
    echo "'DOCKER_INFRASTRUCTURE_HOME' env variable must be set !!!"
    exit -1
  else
    "$1"
  fi;;
  cleanAll|cleanDanglingImages|cleanContainers)
    "$1";;
  *)
  echo "Unkown command entered '$1'"
  exit -1;;
esac
