#
# Build stage
#
FROM maven:3.8.7-openjdk-18-slim as build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

#
# Package stage
#
FROM openjdk:18-jdk-alpine
COPY --from=build /home/app/target/delivery-api-0.0.1.jar /usr/local/lib/delivery-api.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/usr/local/lib/delivery-api.jar"]