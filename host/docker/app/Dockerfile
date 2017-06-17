FROM rpisec-base:latest
MAINTAINER Thomas Herzog <herzog.thomas81@gmail.com>

# arguments
ARG VIDEO_GUID
ARG UID

# user creation
RUN groupmod -g ${VIDEO_GUID} video && \
    useradd --password "" --groups video,sudo --uid ${UID} app && \
    mkdir -p /home/app/app && \
    mkdir -p /home/app/conf && \
    mkdir -p /home/app/log && \
    mkdir -p /home/app/image && \
    mkdir -p /camera

# add dependencies
ADD docker-entrypoint.sh /home/app/docker-entrypoint.sh
ADD app-service.jar /home/app/app/app-service.jar

# switch ownership and make sh executable
RUN chown -R app:video /home/app && \
    chown -R app:video /camera && \
    chmod +x /home/app/*.sh

# switch to app user
USER app

# entrypoint
ENTRYPOINT ~/docker-entrypoint.sh
