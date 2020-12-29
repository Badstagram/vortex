FROM openjdk:15-jdk-alpine3.12

WORKDIR /usr/vortex

COPY target/vortex-bot-1.0.jar .
COPY target/.env .

ENV VORTEX_TOKEN=""
ENV KSOFT_TOKEN=""


CMD ["java", "-jar", "./vortex-bot-1.0.jar"]
