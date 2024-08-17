FROM openjdk:17-slim

LABEL authors="esther"

WORKDIR /app

# Build your application using Maven
# This approach will cache dependencies as a separate layer
RUN mvn clean package -DskipTests

COPY target/paymentservice-0.0.1-SNAPSHOT.jar payment-service.jar

EXPOSE 3000

ENTRYPOINT ["java", "-jar", "app.jar"]