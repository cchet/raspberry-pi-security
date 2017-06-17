FROM rpisec-base:latest
MAINTAINER Thomas Herzog <herzog.thomas81@gmail.com>

# create auth user and it home directories
RUN adduser --disabled-password --gecos "" auth && \
    mkdir /home/auth/app && \
    mkdir /home/auth/conf && \
    mkdir /home/auth/log

# add dependencies
ADD docker-entrypoint.sh ./home/auth/docker-entrypoint.sh
ADD auth-service.jar /home/auth/app/auth-service.jar

# switch ownership and make sh executable
RUN chown auth:auth /home/auth && \
    chmod +x /home/auth/*.sh

# switch to auth user
USER auth

# entrypoint
ENTRYPOINT ~/docker-entrypoint.sh
