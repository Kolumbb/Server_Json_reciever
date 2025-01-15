FROM openjdk:17-jdk-slim

WORKDIR /app

COPY ./NestApi.jar /app/nestApi.jar

VOLUME /app/received_json

EXPOSE 8443

CMD ["java", "-jar", "nestApi.jar"]
