# Stage 1: Build the Spring Boot application using Maven and JDK 21
FROM maven:3-eclipse-temurin-21-alpine AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY . .
RUN mvn clean package -Dmaven.test.skip

# Stage 2: Create a minimal runtime environment using JDK 21
FROM openjdk:21-bullseye
WORKDIR /app

RUN apt-get update && apt-get install -y \
    tesseract-ocr \
    libleptonica-dev \
    && rm -rf /var/lib/apt/lists/*

COPY --from=build /app/target/*.jar app.jar

ENV TESSDATA_PREFIX=/usr/share/tesseract-ocr/4.00/

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
