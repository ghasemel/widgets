# Widgets
A web service to work with widgets via HTTP REST API. The service stores only widgets,
assuming that all clients work with the same board.



## How to build
`mvn clean install`

## How to check code coverage
1. `mvn clean test`
2. open target/site/index.html file in a browser  

## How to run
`java -Dspring.config.location=application.properties -jar widgets-0.0.1-SNAPSHOT.jar`

### Run Profiles
There are four profiles defined in two groups as below:
* Group 1 - App Profile: 
    - `prod`: turn of both debug mode and log in to the console  
    - `dev`: turn on both debug mode and log in to the console
    
* Group 2 - Repository Profile:
    - `h2`: use h2 in-memory database as repository
    - `inMemory`: use In-memory structure as repository
    
_Note: **only one profile from each group** must be chosen **at the same time**_ 

These profiles can be specified in line 3 in application.properties file.   
**Possible choices:**
```
1. spring.profiles.active=prod,h2
2. spring.profiles.active=prod,inMeory
3. spring.profiles.active=dev,h2
4. spring.profiles.active=dev,inMeory
```

## Useful Links
### Health check
[http://localhost:8080/app/actuator/health](http://localhost:8080/app/actuator/health)

### H2 console
* [http://localhost:8080/app/h2-console](http://localhost:8080/app/h2-console)
* database url: `jdbc:h2:mem:testdb`
* database username: `sa`

_**Note: Only with h2 profile can be accessed**_

### API Documentation
[http://localhost:8080/app/swagger-ui/](http://localhost:8080/app/swagger-ui/)

### Sample Requests
Sample [postman](https://www.postman.com/downloads/) collection can be found here:
[widget.postman_collection.json](widget.postman_collection.json)

### Tools/Technologies
* JDK 11
* Spring
* H2
* JUnit 5
* Swagger

### Github Repository Link
[https://github.com/ghasemel/widgets](https://github.com/ghasemel/widgets)