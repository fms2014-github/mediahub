FROM openjdk:8-jdk-alpine
ARG JAR_FILE=build/libs/*.jar
COPY ./media-platform.pkcs12 media-platform.pkcs12
COPY ./media-platform-trust.pkcs12 media-platform-trust.pkcs12
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]