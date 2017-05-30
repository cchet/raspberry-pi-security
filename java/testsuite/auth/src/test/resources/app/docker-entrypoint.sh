#/bin/bash

if [ -z "${VERSION}" ];
then
	echo "environment variable 'APP_VERSION' must be set"
	exit -1
fi

cp -f ~/app/rpisec-app-${VERSION}.jar ~/app/app-${VERSION}-running.jar

exec java ${VERSION} -Duser.home=${HOME} -jar ${HOME}/app/app-${VERSION}-running.jar --spring.profiles.active=test --spring.config.location=file:${HOME}/conf/app-external-application.properties
