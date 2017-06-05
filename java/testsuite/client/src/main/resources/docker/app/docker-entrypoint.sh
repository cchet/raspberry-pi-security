#/bin/bash
# wait for db to initialize
sleep 5s
exec java -Duser.home=${HOME} -jar ${HOME}/app/app.jar --spring.profiles.active=integrationTest --spring.config.location=file:${HOME}/conf/app.properties,file:${HOME}/conf/application-integrationTest.properties