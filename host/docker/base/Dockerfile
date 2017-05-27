FROM resin/rpi-raspbian:jessie
MAINTAINER Thomas Herzog <herzog.thomas81@gmail.com>

# container update and dependency installation
RUN apt-get update -y && \
    apt-get install -y oracle-java8-jdk curl git build-essential gcc cmake

# build and install rpi utils
RUN mkdir /software && \
    cd /software && \
    git clone https://github.com/raspberrypi/userland && \
    cd userland && \
    ./buildme && \
    echo "/opt/vc/lib" > /etc/ld.so.conf.d/userland.conf && \
    ldconfig

# build and install wiringpi
RUN cd /software && \
    git clone git://git.drogon.net/wiringPi && \
    cd wiringPi && \
    ./build

# cleanup
RUN apt-get remove -y curl build-essential cmake gcc git && \
    apt-get autoremove && \
    apt-get autoclean && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/* && \
    rm -rf /software
