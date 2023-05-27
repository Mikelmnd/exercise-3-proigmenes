FROM openjdk:11-jdk-slim

EXPOSE 8080

ARG JAR_FILE=target/Exercise3-0.0.1-SNAPSHOT.jar

ADD ${JAR_FILE} application.jar

LABEL authors="michail"

ENTRYPOINT ["java", "-jar","/application.jar"]