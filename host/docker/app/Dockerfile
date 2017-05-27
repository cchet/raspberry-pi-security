FROM rpisec-rpi-base
MAINTAINER Thomas Herzog <herzog.thomas81@gmail.com>

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
