FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src /app/src
RUN mvn clean package

FROM openjdk:17-jdk-alpine
WORKDIR /usr/local/lib
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8761
CMD ["java","-jar","app.jar"]