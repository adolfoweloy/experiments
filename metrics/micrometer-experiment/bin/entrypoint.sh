#!/usr/bin/env bash
set -x

# host of micrometer-experiment
HOST="172.28.1.1"
# HOST="0.0.0.0"
JMX_PORT=9010

java \
  -Dcom.sun.management.jmxremote \
  -Dcom.sun.management.jmxremote.port=9010 \
  -Dcom.sun.management.jmxremote.rmi.port=9010 \
  -Dcom.sun.management.jmxremote.authenticate=false \
  -Dcom.sun.management.jmxremote.ssl=false \
  -Djava.rmi.server.hostname=${HOST} \
  -Dcom.sun.management.jmxremote.host=${HOST} \
  -jar /usr/src/app/microexp.jar
