#/bin/bash

if [ ! -e ~/app-running.jar ];
then
	mv ~/app/*.jar ~/app/app-running.jar
fi

java -jar ${HOME}/app/app-running.jar --spring.profiles.active=dev --spring.config.location=file:${HOME}/conf/security-application.properties ${APP_JAVA_OPTS}
