FROM resin/rpi-raspbian:jessie
MAINTAINER Thomas Herzog <herzog.thomas81@gmail.com>

RUN apt-get update -y && \
    apt-get install -y oracle-java8-jdk && \
    apt-get install -y wget && \
    rm -rf /var/lib/apt/lists/* && \
    apt-get autoremove && \
    apt-get autoclean && \
    apt-get clean

RUN adduser --disabled-password --gecos "" oauth

ADD docker-entrypoint.sh ./home/oauth/docker-entrypoint.sh
RUN chmod +x /home/oauth/docker-entrypoint.sh && \
    chown oauth:oauth /home/oauth/docker-entrypoint.sh

USER oauth

RUN mkdir ~/app && \
    mkdir ~/conf && \
    mkdir ~/log

ENTRYPOINT ~/docker-entrypoint.sh
