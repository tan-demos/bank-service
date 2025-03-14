# Stage 1: Build the application
FROM gradle:8.12-jdk21-corretto AS builder

WORKDIR /app
# COPY build.gradle.kts settings.gradle.kts ./
# COPY gradle ./gradle
# COPY src ./src
COPY . /app

# Build the application (excluding tests to speed up build)
# RUN gradle build -x test
RUN ./gradlew build -x test

# Stage 2: Create runtime image
FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=builder /app/build/libs/bank-service-1.0.0.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]