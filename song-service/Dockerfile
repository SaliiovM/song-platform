FROM maven:3.8.4-openjdk-17 AS build
COPY src /app/src
COPY pom.xml /app
RUN mvn -f /app/pom.xml clean compile package

FROM openjdk:17-jdk-slim
ARG SONG_SERVICE_SERVER_PORT
COPY --from=build /app/target/song-service-0.0.1.jar /usr/local/lib/song-service.jar
EXPOSE $SONG_SERVICE_SERVER_PORT
ENTRYPOINT ["java","-jar","/usr/local/lib/song-service.jar"]