variables:
  MAVEN_OPTS: "-Dmaven.repo.local=.m2/repository"
  DOCKER_VERSION: "${dockerVersion}"
  MAVEN_VERSION: "${mavenVersion}"
  APP_NAME: "${artifactId}"
  DOCKER_REPO: "${dockerRepository}"

stages:
  - build
  - test
  - package
  - deploy

build:
  stage: build
  image: maven:${mavenVersion}-jdk-${javaVersion}
  script:
    - mvn clean package -DskipTests
  artifacts:
    paths:
      - target/*.jar

test:
  stage: test
  image: maven:${mavenVersion}-jdk-${javaVersion}
  script:
    - mvn test

docker-build:
  stage: package
  image: docker:${DOCKER_VERSION}
  services:
    - docker:${DOCKER_VERSION}-dind
  script:
    - docker build -t $DOCKER_REPO:$CI_COMMIT_SHORT_SHA .
    - docker push $DOCKER_REPO:$CI_COMMIT_SHORT_SHA
  only:
    - main

deploy:
  stage: deploy
  image: alpine/helm:3.7.1
  script:
    - echo "Deploying $APP_NAME version $CI_COMMIT_SHORT_SHA"
    - echo "Would deploy to Kubernetes here"
  only:
    - main
