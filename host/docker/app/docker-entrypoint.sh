#/bin/bash

if [ -z "${APP_VERSION}" ] || [ -z "${RPISEC_DB_NAME}" ] || [ -z "${RPISEC_DB_USER}" ] || [ -z "${RPISEC_DB_PASSWORD}" ] || [ -z "${POSTGRES_HOST}" ];
then
	echo "All env variables mut be given 'APP_VERSION, POSTGRES_HOST, RPISEC_DB_NAME, RPISEC_DB_USER, RPISEC_DB_PASSWORD'"
	exit -1
fi

APP_NAME=rpisec-app-${APP_VERSION}

cp -f ~/app/${APP_NAME}.jar ~/app/app-running.jar

java -jar ${HOME}/app/app-running.jar --spring.profiles.active=prod --spring.config.location=file:${HOME}/conf/security-application.properties ${APP_JAVA_OPTS}
