FROM maven:3.8.4-openjdk-17 as build
LABEL authors="esther"
# Set the working directory inside the container
WORKDIR /app

# Copy the Maven build file (pom.xml) first to fetch dependencies
COPY pom.xml .

# Copy your source files
COPY src src

# Build your application using Maven
# This approach will cache dependencies as a separate layer
RUN mvn clean package -DskipTests

# Copy the built artifact from the target directory
COPY target/itemservice-0.0.1-SNAPSHOT.jar item-service.jar

# Expose the port your app runs on
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "item-service.jar"]