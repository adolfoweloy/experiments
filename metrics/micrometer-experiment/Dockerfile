FROM openjdk:8-jdk

WORKDIR /usr/src/app

ADD target/micrometer-experiment-0.0.1-SNAPSHOT.jar ./microexp.jar
ADD bin/entrypoint.sh ./entrypoint.sh

EXPOSE 8080
EXPOSE 9010

RUN ["chmod", "+x", "/usr/src/app/entrypoint.sh"]
ENTRYPOINT ["./entrypoint.sh"]
