# Stage 1: Build the project with Maven
# We use an official Maven image that includes a JDK to compile our code.
FROM maven:3.8.5-openjdk-11 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml file to download dependencies first (this is a Docker caching optimization)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the rest of the source code
COPY src ./src

# Build the .war file
RUN mvn clean package

# Stage 2: Run the application in Tomcat
# We use an official Tomcat image, which is a lightweight and optimized server.
FROM tomcat:9.0-jdk11-openjdk-slim

# Remove the default webapps that come with Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy the .war file that we built in the previous stage into Tomcat's webapps directory.
# We rename it to ROOT.war so it deploys at the root context path (/).
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

# Expose port 8080 (the default Tomcat port)
EXPOSE 8080

# The command to start the Tomcat server when the container launches
CMD ["catalina.sh", "run"]