# Task

1. User issues a POST request.
2. The server performs heavy calculations on the submitted data. 
3. If we treat the request as usual, we will reach a timeout.

# In order to run the app
1. mvn package
2. java -jar target/dependency/jetty-runner.jar target/*.war
