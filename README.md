# foodie

Multi Tenant Meal Ordering System

To run all serivces as needed use the following command in the terminal

./RunApplication.sh

The naming server / service registry microservice uses EuerkaServer and basically runs first and allows for service discovery. All microservices built must register with the service registry once it powers up.

springdoc-openapi is used for the auto generation of the swagger documentation.

Spring Boot Actuator is used to help monitor and manage the application alongside providing the readiness and liveness health check urls for us. To remove all endpoints that actuator exposes from being public comment out this line in the application.properties file
management.endpoints.web.exposure.include=\*

Zipkin is our distributed tracing server which is fired up via Docker

All request are routed through the api-gateway on port 8765

## URLS on Localhost

```
Zipkin Server
    http://localhost:9411

Eureka Naming Server
    http://localhost:8761

Springdoc Swagger UI
    http://localhost:<port>/swagger-ui/index.html or http://localhost:8765/swagger-ui/index.html
```
