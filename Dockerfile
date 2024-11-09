 FROM openjdk:21
 EXPOSE 6868
 ADD target/notification-docker-compose-0.0.2-SNAPSHOT.jar notifications-app.jar
 ENTRYPOINT ["java", "-jar", "notifications-app.jar"]

### ARG JAR_FILE=target/*.jar
### COPY ${JAR_FILE} notifications-docker-compose-app.jar
### CMD apt-get update -y
### ENTRYPOINT ["java", "-Xmx2048M", "-jar", "/notifications-docker-compose-app.jar"]


