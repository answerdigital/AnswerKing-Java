FROM amazoncorretto:17
COPY target/answer-king-rest-api-1.2.0.jar answerking-1.2.0.jar
ADD ./src/main/resources/application.yaml application.yaml
ENTRYPOINT ["java","-D--spring.config.location=classpath:file:/application-yaml","-jar","/answerking-1.2.0.jar"]