# Stage 1: Build Java API Jar using Maven
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

# Copy source code and build executable jar
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests -B


# Stage 2: Runtime Container
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy built artifact from builder stage
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
