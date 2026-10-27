# Use an official Maven image to build the Spring Boot app
FROM maven:3.9.14-eclipse-temurin-21 AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml and install dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code and build the application
# -DskipTests To skip tests during the build process
COPY src ./src
RUN mvn clean package -DskipTests

# Use an official OpenJDK image to run the application
#FROM eclipse-temurin:21-jre-alpine
FROM eclipse-temurin:21-jre

# Set the working directory
WORKDIR /app


# Copy the built JAR file from the build stage
COPY --from=build /app/target/ToDoList-1.0-SNAPSHOT.jar .

# Expose port 8080
EXPOSE 8080

# Specify the command to run the application
ENTRYPOINT ["java", "-jar", "/app/ToDoList-1.0-SNAPSHOT.jar"]

