FROM library/debian:jessie
MAINTAINER Thomas Herzog <herzog.thomas81@gmail.com>

# container update and dependency installation
RUN apt-get update -y && \
    apt-get install -y oracle-java8-jdk && \
    apt-get autoremove && \
    apt-get autoclean && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# arguments
ARG VIDEO_GUID
ARG UID

# user creation
RUN useradd --password "" rpisec

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
