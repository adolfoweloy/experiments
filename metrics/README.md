# metrics experiments

I've created these experiments to run together as part of the same docker-network.
The main reason for this experiment was to play with docker network and docker compose while trying to access JMX metrics through RMI.

Given a service A which exposes JMX metrics through RMI and a service B that collects those metrics and sends to statsd (here using graphite),
what should be the hostname to be exposed by service A through -Dcom.sun.management.jmxremote.host property?

- localhost?
- 0.0.0.0?
- container name

The answer here was container name. I was expecting that by using 0.0.0.0 would be enough for service B to connect on service A's JMX port.
I was expecting that 0.0.0.0 would allow the service to be reachable by any host name provided by docker container's name.

This experiment might change as I want to play more with micrometer as well.

# technologies used here

- Spring Boot 2
- Spring Actuator
- Micrometer
- retry4j
- java-statsd-client
- graphite
- docker
- docker-compose
- jmx

# how to run the project

- build jmxcollector by running `./gradlew build` within it's directory;
- build micrometer-experiment by running `mvn package` within it's respective directory;
- at the root directory of this experiment, run `docker-compose up --build`.

