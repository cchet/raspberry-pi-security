FROM library/debian:jessie
MAINTAINER Thomas Herzog <herzog.thomas81@gmail.com>

RUN echo "deb http://ppa.launchpad.net/webupd8team/java/ubuntu xenial main" | tee /etc/apt/sources.list.d/webupd8team-java.list && \
    echo "deb-src http://ppa.launchpad.net/webupd8team/java/ubuntu xenial main" | tee -a /etc/apt/sources.list.d/webupd8team-java.list && \
    apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys EEA14886 && \
    apt-get update && \
    echo "oracle-java8-installer shared/accepted-oracle-license-v1-1 select true" | debconf-set-selections && \
    apt-get install -y oracle-java8-installer && \
    apt-get install -y oracle-java8-set-default && \
    apt-get autoremove --purge -y && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*


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