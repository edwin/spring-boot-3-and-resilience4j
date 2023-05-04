# Retry and Failover with Spring Boot 3 and Resilience4j

## About
A sample code to display a retry mechanism when connecting to a third party API provider on Spring Boot using Resilience4j. On this sample, application will retry twice before giving a default response.

## Third Party API
We are using `reqres.in` as a backend API provider. 

## Simulate an IO Exception
```java
    public String getUser(Integer id) throws IOException {

        if(new Random().nextBoolean()) {
            logger.debug("==== simulate random exception ====");
            throw new IOException();
        }

        ResponseEntity<String> users = restTemplate.getForEntity("https://reqres.in/api/user/"+id, String.class);
        return users.getBody();
    }
```

## Retry Mechanism
```java
    @GetMapping(path = "/user/{id}")
    @Retry(name = "instance1", fallbackMethod = "fallbackGetUser")
    public String getUser(@PathVariable("id") Integer id) throws IOException {
        return userService.getUser(id);
    }

    public String fallbackGetUser(Integer id, Exception ex) {
        logger.debug("==== giving default response ====");
        return "{}";
    }
```

## Configuration
We set two retry attempts before giving a default fallback response, each with a 3seconds wait between retry.
```
# retry
resilience4j.retry.instances.instance1.max-attempts=2
resilience4j.retry.instances.instance1.wait-duration=3s
```

## Logs
```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.0.4)

03-05-2023 19:08:52 [main] INFO  com.edw.Main.logStarting - Starting Main using Java 17.0.6 with PID 21360 (D:\source\spring-boot-resilience4j-retry\target\classes started by thinkpad in D:\source\spring-boot-resilience4j-retry)
03-05-2023 19:08:52 [main] DEBUG com.edw.Main.logStarting - Running with Spring Boot v3.0.4, Spring v6.0.6
03-05-2023 19:08:52 [main] INFO  com.edw.Main.logStartupProfileInfo - No active profile set, falling back to 1 default profile: "default"
03-05-2023 19:08:53 [main] INFO  org.springframework.boot.web.embedded.tomcat.TomcatWebServer.initialize - Tomcat initialized with port(s): 8080 (http)
03-05-2023 19:08:53 [main] INFO  org.apache.catalina.core.StandardService.log - Starting service [Tomcat]
03-05-2023 19:08:53 [main] INFO  org.apache.catalina.core.StandardEngine.log - Starting Servlet engine: [Apache Tomcat/10.1.5]
03-05-2023 19:08:53 [main] INFO  org.apache.catalina.core.ContainerBase.[Tomcat].[localhost].[/].log - Initializing Spring embedded WebApplicationContext
03-05-2023 19:08:53 [main] INFO  org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.prepareWebApplicationContext - Root WebApplicationContext: initialization completed in 1290 ms
03-05-2023 19:08:54 [main] INFO  org.springframework.boot.actuate.endpoint.web.EndpointLinksResolver.<init> - Exposing 23 endpoint(s) beneath base path '/actuator'
03-05-2023 19:08:54 [main] INFO  org.springframework.boot.web.embedded.tomcat.TomcatWebServer.start - Tomcat started on port(s): 8080 (http) with context path ''
03-05-2023 19:08:54 [main] INFO  com.edw.Main.logStarted - Started Main in 2.77 seconds (process running for 3.253)
03-05-2023 19:08:57 [http-nio-8080-exec-1] INFO  org.apache.catalina.core.ContainerBase.[Tomcat].[localhost].[/].log - Initializing Spring DispatcherServlet 'dispatcherServlet'
03-05-2023 19:08:57 [http-nio-8080-exec-1] INFO  org.springframework.web.servlet.DispatcherServlet.initServletBean - Initializing Servlet 'dispatcherServlet'
03-05-2023 19:08:57 [http-nio-8080-exec-1] INFO  org.springframework.web.servlet.DispatcherServlet.initServletBean - Completed initialization in 1 ms
03-05-2023 19:09:06 [http-nio-8080-exec-4] DEBUG com.edw.service.UserService.getUser - ==== simulate random exception ====
03-05-2023 19:09:09 [http-nio-8080-exec-4] DEBUG com.edw.service.UserService.getUser - ==== simulate random exception ====
03-05-2023 19:09:09 [http-nio-8080-exec-4] DEBUG com.edw.controller.IndexController.fallbackGetUser - ==== giving default response ====
```