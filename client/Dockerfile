FROM openjdk:11-slim

# VOLUME ["/tmp", "/var/cigs-db"]
EXPOSE 80

ARG JAR_FILE=target/client-*.jar
ADD ${JAR_FILE} client.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-jar","/client.jar",  "--spring.profiles.active=prod"]


