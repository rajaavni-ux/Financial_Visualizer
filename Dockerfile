# Step 1: Build using Maven
FROM maven:3.8.4-openjdk-17 AS build
COPY . .
RUN mvn clean install -DskipTests

# Step 2: Run using a guaranteed stable image
FROM eclipse-temurin:17-jre-focal

# Tesseract install karne ka sabse sahi tarika Linux par
RUN apt-get update && apt-get install -y \
    tesseract-ocr \
    libtesseract-dev \
    && rm -rf /var/lib/apt/lists/*

COPY --from=build /target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
