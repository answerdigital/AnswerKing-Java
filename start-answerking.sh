#!/bin/bash

docker-compose down --remove-orphans
mvn clean install
docker-compose build
docker-compose up
