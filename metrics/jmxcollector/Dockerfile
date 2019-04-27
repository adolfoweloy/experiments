FROM openjdk:8-jdk

WORKDIR /usr/src/app

ADD build/libs/jmxcollector-1.0-SNAPSHOT.jar ./jmxcollector.jar

ENTRYPOINT ["java", "-jar", "./jmxcollector.jar"]
