FROM rpisec-test-base:latest
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

# user creation
RUN adduser --disabled-password --gecos "" user

# add dependencies
ADD app.jar /home/user/app/app.jar

RUN chown -R user:user /home/user

# switch to app user
USER user

# user home config
RUN mkdir -p ~/conf && \
    mkdir -p ~/log && \
    mkdir -p ~/image

# cmd
CMD exec java -Duser.home=${HOME} -jar ${HOME}/app/app.jar --spring.profiles.active=test
