# telepass-challenge
An example of a RESTful WebServer developed using SpringBoot.

This simple server will store customers and devices, and will give the possibility to add/delete/update those entities.



## Dependencies
There are a number of third-party dependencies used in the project. Browse the Maven pom.xml file for details of libraries and versions used.

## Building the project
You will need:

*	Java JDK 17 or higher
*	Maven 3.8.1 or higher
*	Git

Clone the project and use Maven to build and run the application with this command:

	$ mvn clean verify spring-boot:run



### Browser URL
Open your browser at the following URL for Swagger UI (giving REST interface details):

http://localhost:8080/swagger-ui.html



Open your browser at the following URL for H2 database UI  (giving database data details):
Use the jdbc string "jdbc:h2:mem:telepass-challenge-db" to connect.

http://localhost:8080/h2-console




