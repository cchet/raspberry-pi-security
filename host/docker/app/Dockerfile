FROM FROM resin/rpi-raspbian:jessie
MAINTAINER Thomas Herzog <herzog.thomas81@gmail.com>

RUN apt-get update -y && \
	  apt-get install -y oracle-java8-jdk && \
    apt-get clean

RUN adduser rpisec --disabled-password
RUN cd /home/rpisec
ADD docker-entrypoint.sh ./
RUN chown rpisec:rpisec ./docker-entrypoint.sh && \
    chmod +x ./docker-entrypoint.sh

USER rpisec

RUN mkdir app && \
    mkdir conf

ENTRYPOIT ./docker-entrypoint.sh
