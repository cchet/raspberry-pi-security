FROM resin/rpi-raspbian:jessie
MAINTAINER Thomas Herzog <herzog.thomas81@gmail.com>

# Container update and dependency installation
RUN apt-get update -y && \
    apt-get install -y oracle-java8-jdk \
        curl git gcc cmake build-essential python python-dev python-pip python-virtualenv && \
    pip install pyserial

# Download all repositories
RUN mkdir /software && \
    cd /software && \
    git clone --depth 1 https://github.com/raspberrypi/userland.git && \
    git clone git://git.drogon.net/wiringPi

# Build and install wiringPi
RUN cd /software/wiringPi && \
    ./build && \
    pip install wiringpi2

# Build and install userland
RUN cd /software/userland && \
    ./buildme /usr/bin

# cleanup
RUN apt-get remove -y curl git gcc cmake build-essential python-dev python-pip && \
    rm -rf /var/lib/apt/lists/* && \
    apt-get autoremove && \
    apt-get autoclean && \
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
