# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk


# Set the working directory in the container
WORKDIR /usr/app

# Copy the jar file into the container
COPY ./target/SchoolManagementSystem-0.0.1-SNAPSHOT.jar .

# Ensure the jar file is executable (this step is often optional)
RUN sh -c 'touch SchoolManagementSystem-0.0.1-SNAPSHOT.jar'

# Expose the port the application runs on
EXPOSE 8080

# Set the command to run the jar file
CMD ["java", "-jar", "SchoolManagementSystem-0.0.1-SNAPSHOT.jar"]
