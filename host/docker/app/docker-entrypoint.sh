#/bin/bash

exec sudo java ${APP_JAVA_OPTS} -Duser.home=${HOME} -jar ${HOME}/app/app-service.jar --spring.profiles.active=prod --spring.config.location=file:${HOME}/conf/app-external-application.properties
