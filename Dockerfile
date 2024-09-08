# Stage 1: Build the application
FROM maven:3.8.8  AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the project files to the working directory
COPY pom.xml /app


# Package the application
RUN mvn dependency:resolve
COPY . /app
RUN mvn clean
RUN mvn package -DskipTests
# Stage 2: Run the application
FROM eclipse-temurin:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port the app runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
