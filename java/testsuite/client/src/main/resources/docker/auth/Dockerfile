FROM rpisec-test-base:latest
MAINTAINER Thomas Herzog <herzog.thomas81@gmail.com>


RUN adduser --disabled-password --gecos "" user && \
    mkdir ~/conf && \
    mkdir ~/log

ADD docker-entrypoint.sh /home/user/docker-entrypoint.sh
ADD app.jar ./home/user/app/app.jar
ADD application-integrationTest.properties /home/user/conf/application-integrationTest.properties
ADD auth.properties /home/user/conf/auth.properties
ADD firebase-account.json /home/user/conf/firebase-account.json

RUN chmod +x /home/user/docker-entrypoint.sh && \
    chown -R user:user /home/user

USER user

# entrypoint
ENTRYPOINT ~/docker-entrypoint.sh