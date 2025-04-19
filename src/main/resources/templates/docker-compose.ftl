version: '3.8'

services:
  ${serviceName}:
    build: .
    ports:
      - "${port}:${port}"
    restart: always
    environment:
      - SPRING_PROFILES_ACTIVE=prod