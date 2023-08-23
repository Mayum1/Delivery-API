#
# Build stage
#
FROM maven:3.8.7-openjdk-18-slim
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

#
# Package stage
#
FROM openjdk:11-jre-slim
COPY --from=build /home/app/target/delivery-api-0.0.1.jar /usr/local/lib/delivery-api.jar
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/usr/local/lib/delivery-api.jar"]