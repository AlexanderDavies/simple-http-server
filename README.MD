# Support for Java 11+

**TO DO** 
- Parse the response into HTTP Request
- Map the body and params with the request args 
- Convert to Gradle
- Add comprehensive unit tests and integration tests
- Parse and cast body of request for post, put, patch
- Revisit throwing exception from Request, can be more specific with illegal argument exception
- Add more advanced logging
- Revisit 'stopping of the process', how to do this properly + volatile? 
- Create HTTPS socket factory
- Executor service support for queue so that its bounded elastic
- Revisit unchecked cast exceptions in Routes