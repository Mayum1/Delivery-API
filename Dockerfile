FROM adoptopenjdk:17-jre-hotspot

WORKDIR /app

COPY target/delivery-api-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]