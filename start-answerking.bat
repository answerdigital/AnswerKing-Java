@echo off

:: If this script does not work for you please ensure you install
:: Maven onto your machine, https://maven.apache.org/install.html

echo ================================================
echo Beginning Docker Build
echo ================================================

call mvn clean install
call docker-compose down --remove-orphans
call docker-compose build
call docker-compose up
