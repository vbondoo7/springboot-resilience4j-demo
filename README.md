# Using Resilience4j with Spring Cloud Circuit Breaker

This project shows how to leverage [Resilience4j](https://github.com/resilience4j/resilience4j)
together with [Spring Cloud Circuit Breaker](https://spring.io/projects/spring-cloud-circuitbreaker).

Spring Cloud Circuit Breaker (SCCB) is a young project from the Spring Framework.
Using SCCB you get an abstraction layer over many circuit breaker implementations, such as:
 - Resilience4j
 - [Spring Retry](https://github.com/spring-projects/spring-retry)
 - [Sentinel](https://github.com/alibaba/Sentinel)
 - [Netflix Hystrix](https://github.com/Netflix/Hystrix) (now in [maintenance mode](https://github.com/Netflix/Hystrix#hystrix-status))

This way you can easily switch from an implementation to another one,
with no to low impact in your source code.

Let's see how you can leverage Resilience4j with your Spring Boot app.

## How to use it?

Compile this app using a JDK 11+:
```bash
$ ./mvnw clean package
```

Start the app:
```bash
$ java -jar target/springboot-resilience4j-demo.jar
```

## Refer - https://reflectoring.io/retry-with-springboot-resilience4j/
