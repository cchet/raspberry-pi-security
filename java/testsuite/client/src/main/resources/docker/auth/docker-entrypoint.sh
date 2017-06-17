#/bin/bash
# wait for db to initialize
sleep ${WAIT_INTERVAL}s
exec java -Duser.home=${HOME} -jar ${HOME}/app/app.jar --spring.profiles.active=integrationTest --spring.config.location=file:${HOME}/conf/auth.properties,file:${HOME}/conf/application-integrationTest.properties
