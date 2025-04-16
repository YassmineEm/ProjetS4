version: '3.8'

services:
  ${serviceName}:
    build: .
    ports:
      - "${port}:${port}"
    volumes:
      - .:/app
    command: ["java", "-jar", "/app/target/${serviceName}.jar"]
