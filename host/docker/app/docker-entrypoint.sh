#/bin/bash

if [ -z "${RPISEC_VERSION}" ];
then
	echo "environment variable 'APP_VERSION' must be set"
	exit -1
fi

cp -f ~/app/rpisec-app-${RPISEC_VERSION}.jar ~/app/app-${RPISEC_VERSION}-running.jar

java -jar ${HOME}/app/app-${RPISEC_VERSION}-running.jar --spring.profiles.active=prod --spring.config.location=file:${HOME}/conf/app-external-application.properties ${APP_JAVA_OPTS}
