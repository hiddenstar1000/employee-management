# Stage 1: Runtime Java 25 image
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

# Copy pre-compiled Spring Boot executable jar
COPY target/*.jar app.jar

# Expose Spring Boot default HTTP port
EXPOSE 8080

# Launch executable jar with active spring profile support
ENTRYPOINT ["java", "-jar", "app.jar"]
