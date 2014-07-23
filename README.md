# Task

1. User issues a POST request.
2. The server performs heavy calculations on the submitted data. 
3. If we treat the request as usual, we will reach a timeout.

# In order to run the app
1. mvn package
2. java -jar target/dependency/jetty-runner.jar target/*.war

# Use case
1. Enter any number (for example, 100) and press Submit
2. The request is sent to the server
3. Server starts to emulate a heavy task (it will be executing somewhere around the number of seconds that you entered)
4. The user immediately gets the reponse and the task is executed asynchronously
5. AJAX requests start to poll the server and get information about task's progress
6. When the task is finished, some result is shown to the user
