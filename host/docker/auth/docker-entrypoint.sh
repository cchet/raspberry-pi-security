#/bin/bash

exec java ${OAUTH_JAVA_OPTS} -jar ${HOME}/app/auth-service.jar --spring.profiles.active=prod --spring.config.location=file:${HOME}/conf/auth.properties
