# Answer King REST API

The Answer King REST API is the backend for a fast food business. It allows products and categories to be created and 
orders to be placed. An product can be included in multiple categories.

## Docker

To run the project using docker, navigate to the project directory 
and execute the following commands:

### Linux / MacOS

1. `./mvnw clean install`
2. `docker-compose build`
3. `docker-compose up`

### Windows

1. `.\mvnw.cmd clean install`
2. `docker-compose build`
3. `docker-compose up`

### Prerequisites

[Docker](https://docs.docker.com/get-docker/)

[Docker Compose](https://docs.docker.com/compose/install/)

[AMAZON CORRETTO 17](https://docs.aws.amazon.com/corretto/latest/corretto-17-ug/downloads-list.html/)

## Maven 

To run the project using the Spring Boot Maven Plugin update the datasource properties for the dev profile in 
*src/main/resources/application.yaml*.

Then, navigate to the project directory and enter the following command:

1. `mvn clean compile`
2. `mvn spring-boot:run -D spring-boot.run.profiles=dev`

If Maven isn't installed on your system, the Maven Wrapper can be used by using the following commands:

**Linux / MacOS**

1. `./mvnw clean compile`
2. `./mvnw spring-boot:run -D spring-boot.run.profiles=dev`

**Windows**

1. `.\mvnw.cmd clean compile`
2. `.\mvnw.cmd spring-boot:run -D spring-boot.run.profiles=dev`

### Prerequisites

[AMAZON CORRETTO 17](https://docs.aws.amazon.com/corretto/latest/corretto-17-ug/downloads-list.html/)

## MySQL

To set up passwords for MySQL, navigate to the project directory and open the *.env* file. Then set passwords for:

1. `MYSQLDB_ROOT_PASSWORD=`
2. `MYSQLDB_PASSWORD=`

## Security

All requests require HTTP Basic authentication, the users are *paul*, *john*, *george* and *ringo*. 
The password is *secret*.




