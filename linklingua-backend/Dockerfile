# ============================================================
# LinkLingua backend (Spring Boot 3.2.5 / Java 21) image
# Multi-stage build: Maven build stage -> slim JRE runtime stage
# ============================================================

# ---------- Stage 1: build the executable jar ----------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Resolve dependencies first so they are cached across source-only changes
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Build the application (tests skipped to keep the image build fast/deterministic)
COPY src ./src
RUN mvn -B clean package -DskipTests

# ---------- Stage 2: minimal runtime ----------
FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

# Directory where uploaded files are stored (backed by a volume in compose)
RUN mkdir -p /app/uploads

# Copy the fat jar produced by the build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Allow extra JVM flags via JAVA_OPTS without rebuilding the image
ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
