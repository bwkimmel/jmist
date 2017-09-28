FROM alpine:3.6

RUN apk --no-cache add \
    build-base \
    protobuf \
    python3 \
    xmlstarlet \
    zip \
  && python3 -m ensurepip \
  && pip3 install virtualenv

WORKDIR /src/jmist-blender
