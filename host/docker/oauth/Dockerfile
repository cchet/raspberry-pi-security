FROM rpisec-base:latest
MAINTAINER Thomas Herzog <herzog.thomas81@gmail.com>

RUN adduser --disabled-password --gecos "" oauth

ADD docker-entrypoint.sh ./home/oauth/docker-entrypoint.sh
RUN chmod +x /home/oauth/docker-entrypoint.sh && \
    chown oauth:oauth /home/oauth/docker-entrypoint.sh

USER oauth

RUN mkdir ~/app && \
    mkdir ~/conf && \
    mkdir ~/log

ENTRYPOINT ~/docker-entrypoint.sh
