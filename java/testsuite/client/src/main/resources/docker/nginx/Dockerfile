FROM rpisec-test-base:latest
MAINTAINER Thomas Herzog <herzog.thomas81@gmail.com>

RUN apt-get update -y && \
    apt-get install -y ca-certificates nginx && \
    rm -rf /var/lib/apt/lists/* && \
    apt-get autoremove && \
    apt-get autoclean && \
    apt-get clean

RUN ln -sf /dev/stdout /var/log/nginx/access.log
RUN ln -sf /dev/stderr /var/log/nginx/error.log
RUN mkdir /cert
ADD nginx.conf /etc/nginx/nginx.conf

CMD ["nginx", "-g", "daemon off;"]
