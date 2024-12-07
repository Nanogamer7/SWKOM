# Stage 1: Build the Spring Boot application using Maven and JDK 21
FROM maven:3-eclipse-temurin-21-alpine AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY . .
RUN mvn clean package -Dmaven.test.skip

# Stage 2: Create a minimal runtime environment using JDK 21
FROM openjdk:21
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
