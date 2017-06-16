FROM rpisec-test-base:latest
MAINTAINER Thomas Herzog <herzog.thomas81@gmail.com>

# user creation
RUN adduser --disabled-password --gecos "" user && \
    mkdir -p /home/user/conf && \
    mkdir -p /home/user/log && \
    mkdir -p /home/user/image

# add dependencies
ADD docker-entrypoint.sh /home/user/docker-entrypoint.sh
ADD app.jar /home/user/app/app.jar
ADD application-integrationTest.properties /home/user/conf/application-integrationTest.properties
ADD app.properties /home/user/conf/app.properties
ADD firebase-account.json /home/user/conf/firebase-account.json

RUN chmod +x /home/user/docker-entrypoint.sh && \
    chown -R user:user /home/user

# switch to app user
USER user

# entrypoint
ENTRYPOINT ~/docker-entrypoint.sh