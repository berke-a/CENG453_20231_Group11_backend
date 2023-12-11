# Use a Maven base image to build the project
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
# Copy the source code into the image
COPY src ./src
COPY pom.xml .
# Run Maven to install the dependencies and package the application
RUN mvn clean package

# Use Eclipse Temurin JDK to run the application
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
# Copy the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
EXPOSE 8080
