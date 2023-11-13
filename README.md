# CENG453 20231 Group11 Backend

This is the Group11 backend application of the CENG453 Term Project. In this backend application, Java 17 is used with Spring Boot v3.1.5. For documentation Springdoc Swagger-ui is utilized. JUnit v4.13.2 is used while creating the unit tests.

## How to run the application

After cloning the project to your local machine, in the project directory, use `mvn clean install` command on your terminal to build the backend application. Then use `java -jar target/CENG453_20231_Group11_backend-0.0.1-SNAPSHOT.jar` to run the application that you built. 

## Database

There are 3 entities in the project which are 'user', 'score', and 'password_reset_token'. The fields and relation between this entities can be seen from the `ER-diagram.png` inside the root folder.

## Documentation

`http://localhost:8080/swagger-ui/index.html` can be used to reach the API documentation of the project after running the application as stated above.
