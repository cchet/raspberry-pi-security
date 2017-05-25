FROM resin/rpi-raspbian:jessie
MAINTAINER Thomas Herzog <herzog.thomas81@gmail.com>

RUN apt-get update -y && \
    apt-get install -y oracle-java8-jdk && \
    apt-get install -y wget && \
    rm -rf /var/lib/apt/lists/* && \
    apt-get autoremove \
    apt-get autoclean \
    apt-get clean

RUN adduser --disabled-password --gecos "" rpisec

ADD docker-entrypoint.sh ./home/rpisec/docker-entrypoint.sh
RUN chmod +x /home/rpisec/docker-entrypoint.sh && \
    chown rpisec:rpisec /home/rpisec/docker-entrypoint.sh

USER rpisec

RUN mkdir ~/app && \
    mkdir ~/conf && \
    mkdir ~/log && \
    mkdir ~/image

ENTRYPOINT ~/docker-entrypoint.sh
