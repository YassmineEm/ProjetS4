image: maven:3.8.1-jdk-${javaVersion}

stages:
  - build
  - test
  - docker

variables:
  MAVEN_CLI_OPTS: "-s .m2/settings.xml --batch-mode"
  DOCKER_DRIVER: overlay2

cache:
  paths:
    - .m2/repository

before_script:
  - echo "Start pipeline for ${artifactId}..."

build:
  stage: build
  script:
    - mvn clean package -DskipTests

test:
  stage: test
  script:
    - mvn test

docker-build:
  stage: docker
  image: docker:latest
  services:
    - docker:dind
  script:
    - docker build -t $CI_REGISTRY_IMAGE:${artifactId} .
    - docker push $CI_REGISTRY_IMAGE:${artifactId}
  only:
    - main
