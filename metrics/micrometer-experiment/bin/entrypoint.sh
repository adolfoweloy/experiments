#!/usr/bin/env bash
set -x

JMX_PORT=9010
#HOST="0.0.0.0"
HOST="microexp"

java \
  -Dcom.sun.management.jmxremote.local.only=false \
  -Dcom.sun.management.jmxremote.ssl=false \
  -Dcom.sun.management.jmxremote.authenticate=false \
  -Dcom.sun.management.jmxremote.port=${JMX_PORT} \
  -Dcom.sun.management.jmxremote.rmi.port=${JMX_PORT} \
  -Dcom.sun.management.jmxremote.host=${HOST} \
  -Djava.rmi.server.hostname=${HOST} \
  -jar /usr/src/app/microexp.jar
