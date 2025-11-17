# Stage 1: Build with Gradle
FROM gradle:8.12.0-jdk21-alpine AS builder
WORKDIR /app
COPY --chown=gradle:gradle . .
RUN gradle clean build -x test --no-daemon

# Stage 2: Runtime with minimal JRE
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]