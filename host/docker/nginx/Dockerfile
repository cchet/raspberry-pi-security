FROM resin/rpi-raspbian:jessie
MAINTAINER Thomas Herzog <herzog.thomas81@gmail.com>

RUN apt-get update -y && \
    apt-get install -y ca-certificates nginx && \
    rm -rf /var/lib/apt/lists/* && \
    apt-get clean

RUN adduser nginx
USER nginx
RUN cd ~ && \
    mkdir cert

# forward request and error logs to docker log collector
RUN ln -sf /dev/stdout /var/log/nginx/access.log
RUN ln -sf /dev/stderr /var/log/nginx/error.log

CMD ["nginx", "-g", "daemon off;"]
