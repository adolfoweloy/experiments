FROM openjdk:8-jdk

WORKDIR /usr/src/app

RUN wget https://github.com/jiaqi/jmxterm/releases/download/v1.0.0/jmxterm-1.0.0-uber.jar && \
    mv jmxterm-1.0.0-uber.jar jmxterm.jar

ADD build/libs/jmxcollector-1.0-SNAPSHOT.jar ./jmxcollector.jar

ENTRYPOINT ["java", "-jar", "./jmxcollector.jar"]
