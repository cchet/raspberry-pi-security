#/bin/bash

if [ -z "${APP_VERSION}" ];
then
	echo "environment variable 'APP_VERSION' must be set"
	exit -1
fi

APP_NAME=rpisec-app-${APP_VERSION}

cp -f ~/app/${APP_NAME}.jar ~/app/app-running.jar

java -jar ${HOME}/app/app-running.jar --spring.profiles.active=prod --spring.config.location=file:${HOME}/conf/external-application.properties  ${APP_JAVA_OPTS}
