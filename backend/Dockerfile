# Stage 1: Build the Spring Boot application using Maven and JDK 21
FROM maven:3-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY . .
RUN mvn clean package -Dmaven.test.skip

# Stage 2: Create a minimal runtime environment using JDK 21
FROM openjdk:21
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Expose the port that the Spring Boot application listens on
EXPOSE 8081

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
