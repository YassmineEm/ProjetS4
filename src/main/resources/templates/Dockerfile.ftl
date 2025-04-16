FROM openjdk:${javaVersion}-jdk-slim
COPY target/${artifactId}.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
