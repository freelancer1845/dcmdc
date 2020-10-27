FROM openjdk:11-slim

# VOLUME ["/tmp", "/var/cigs-db"]
EXPOSE 8080
EXPOSE 18080

ARG JAR_FILE=target/server-*.jar
ADD ${JAR_FILE} server.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-jar","/server.jar",  "--spring.profiles.active=prod"]


