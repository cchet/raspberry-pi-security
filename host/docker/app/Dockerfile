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

# arguments
ARG VIDEO_GUID
ARG UID

# user creation
RUN groupmod -g ${VIDEO_GUID} video && \
    useradd --password "" --groups video,sudo --uid ${UID} rpisec

# add dependencies
ADD docker-entrypoint.sh /home/rpisec/docker-entrypoint.sh

# switch ownership and create camera device used image location
RUN mkdir -p /camera && \
    chown -R rpisec:video /home/rpisec && \
    chmod +x /home/rpisec/*.sh

# switch to app user
USER rpisec

# user home config
RUN mkdir -p ~/app && \
    mkdir -p ~/conf && \
    mkdir -p ~/log && \
    mkdir -p ~/image

# entrypoint
ENTRYPOINT ~/docker-entrypoint.sh
