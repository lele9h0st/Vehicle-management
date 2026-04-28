# Use an official JDK 25 image as a parent image
FROM eclipse-temurin:25-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the executable jar file to the container
COPY target/keyloop-0.0.1-SNAPSHOT.jar app.jar

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
