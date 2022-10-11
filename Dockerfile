FROM openjdk:17-jdk-alpine
MAINTAINER benjamin.hession@answerdigital.com
COPY target/answer-king-rest-api-0.0.1.jar answerking-0.0.1.jar
ADD ./src/main/resources/application.yaml application.yaml
ENTRYPOINT ["java","-D--spring.config.location=classpath:file:/application-yaml","-jar","/answerking-0.0.1.jar"]