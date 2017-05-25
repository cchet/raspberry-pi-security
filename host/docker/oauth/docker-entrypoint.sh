#/bin/bash

if [ -z "${RPISEC_VERSION}" ];
then
	echo "environment variable 'RPISEC_VERSION' must be set"
	exit -1
fi

cp -f ~/app/rpisec-oauth-${RPISEC_VERSION}.jar ~/app/oauth-${RPISEC_VERSION}-running.jar

java -jar ${HOME}/app/oauth-${RPISEC_VERSION}-running.jar --spring.profiles.active=prod --spring.config.location=file:${HOME}/conf/oauth-external-application.properties ${APP_JAVA_OPTS}
