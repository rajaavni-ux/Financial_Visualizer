# Step 1: Build the application
FROM maven:3.8.4-openjdk-17 AS build
COPY . .
RUN mvn clean install -DskipTests

# Step 2: Run the application
# Maine yahan image ka naam change kiya hai jo stable hai
FROM openjdk:17-oracle 
RUN microdnf install -y tesseract tesseract-langpack-eng && microdnf clean all

COPY --from=build /target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
