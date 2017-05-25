#/bin/bash

DOCKER_HOME=/home/docker
HET_HOME=/home/het

# Setup host system
apt-get update -y
apt-get upgrade -y
apt-get install -y git
apt-get install -y rpi-update
apt-get install -y wiringpi
# kernel downgrade because of pi4j issue
rpi-update 52241088c1da59a359110d39c1875cda56496764
apt-get autoremove
apt-get clean
apt-get autoclean

# Create users
adduser --disabled-login --gecos "" docker
adduser --disabled-password --gecos "" het

# Create directories
mkdir ~/repos
mkdir -p ${HET_HOME}/.ssh
mkdir -p ${DOCKER_HOME}/.ssh
mkdir -p ${DOCKER_HOME}/repos
mkdir -p ${DOCKER_HOME}/docker-volumes/nginx/logs
mkdir -p ${DOCKER_HOME}/docker-volumes/nginx/cert
mkdir -p ${DOCKER_HOME}/docker-volumes/postgres
mkdir -p ${DOCKER_HOME}/docker-volumes/app/app
mkdir -p ${DOCKER_HOME}/docker-volumes/app/conf
mkdir -p ${DOCKER_HOME}/docker-volumes/app/log
mkdir -p ${DOCKER_HOME}/docker-volumes/app/image
mkdir -p ${DOCKER_HOME}/docker-volumes/oauth/app
mkdir -p ${DOCKER_HOME}/docker-volumes/oauth/conf
mkdir -p ${DOCKER_HOME}/docker-volumes/oauth/log

cp authorized_keys ${HET_HOME}/.ssh/

chown -R ${DOCKER_HOME} docker:docker
chown -R ${HET_HOME} het:het

runuser -l het -c "ssh-keygen -t rsa -b 8192 -C 'het@rpisec.at' -f ~/ -q -P ''"
runuser -l het -c "chmod 400 ~/.ssh/id_rsa*"
runuser -l docker -c "ssh-keygen -t rsa -b 8192 -C 'docker@rpisec.at'-q -P ''"
runuser -l docker -c "chmod 400 ~/.ssh/id_rsa*"

# checkout reposiroties
git clone https://github.com/certbot/certbot.git ~/repos
