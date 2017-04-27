#/bin/bash

mv ~/app/*.jar ~/app/app-running.jar

java -jar ${HOME}/app/app-running.jar --spring.profiles.active=prod --spring.config.location=file:${HOME}/conf/security-application.properties ${APP_JAVA_OPTS}
