FROM openjdk:17

WORKDIR /app

COPY /target/assignment-0.0.1-SNAPSHOT.jar /app/assignment.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/assignment.jar"]