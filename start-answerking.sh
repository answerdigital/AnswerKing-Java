#!/bin/bash

# If this script does not work for you please ensure you install
# Maven onto your machine, https://maven.apache.org/install.html

echo "================================================"
echo "Beginning Docker Build on Linux"
echo "================================================"

mvn clean install
docker-compose down --remove-orphans
docker-compose build
docker-compose up
