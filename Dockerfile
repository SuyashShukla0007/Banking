# syntax=docker/dockerfile:1

# Build stage
FROM maven:3.9.9-eclipse-temurin-17-alpine AS builder
WORKDIR /app

# Cache dependencies first
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

# Build application
COPY src ./src
RUN mvn -q -DskipTests clean package

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Run as non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy built JAR from target folder
COPY --from=builder /app/target/*.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
