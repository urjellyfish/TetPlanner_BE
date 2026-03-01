# -------- STAGE 1: Build --------
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copy pom trước để tận dụng cache layer
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source
COPY src ./src

# Build jar
RUN mvn clean package -DskipTests


# -------- STAGE 2: Runtime --------
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Tạo user không phải root (production best practice)
RUN addgroup -S spring && adduser -S spring -G spring

# Copy jar từ stage build
COPY --from=build /app/target/*.jar app.jar

# Set ownership
RUN chown spring:spring app.jar

USER spring

EXPOSE 8080

# JVM tối ưu cho container
ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-XX:+UseG1GC", \
  "-jar", "app.jar"]