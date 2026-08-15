# Stage 1: Build Java API Jar using Maven
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build executable jar
COPY src ./src
RUN mvn package -DskipTests -B

# Stage 2: Runtime Container
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy built artifact from builder stage
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
