# Online-shopping

https://start.spring.io/

Cannot resolve symbol `SpringApplication`
his means your application is missing a required property in the configuration file that tells Spring where to import additional configuration from, typically a central config server.
Add a spring.config.import=configserver: Property to Your Configuration


For all services, consider these common dependencies:

**Spring Web**: For creating web applications (includes RESTful services using Spring MVC).
**Spring Security**: For securing your applications.

Specific Dependencies for Each Service
**Item Service** (using MongoDB)
Spring Data MongoDB: To integrate MongoDB database operations.
Spring Boot Actuator: For monitoring and managing your application.
**Order Service** (using Cassandra)
Spring Data Cassandra: To handle Cassandra database operations.
Spring for Apache Kafka: To produce and consume messages effectively in an event-driven architecture.
**Payment Service** (using MySQL)
Spring Data JPA: To facilitate access to relational data.
MySQL Driver: As the JDBC driver for MySQL database interactions.
**Account Service** (using PostgreSQL)
Spring Data JPA: Similar to the Payment Service for handling database operations.
PostgreSQL Driver: As the JDBC driver for PostgreSQL database interactions.

Additional Dependencies for Microservices Architecture
Spring Cloud Config Client: For external configuration management.
Eureka Discovery Client: If you are using Netflix Eureka for service discovery.
Spring Cloud OpenFeign: For declarative REST client to simplify calls to other services.
Spring Cloud Gateway: If you need a gateway for routing and filtering requests to your microservices.
Spring Boot Actuator: For insights into your application via health-checks, metrics, etc.
Spring Cloud LoadBalancer: For client-side load balancing.
Spring Cloud Circuit Breaker: For fault tolerance (with Resilience4J or Hystrix).
Spring Cloud Stream: For building applications connected by shared messaging systems.
For Testing and Documentation
Spring Boot Test: Typically included by default for testing capabilities.
Springfox Swagger 2: For API documentation (if not using the newer OpenAPI 3).


**Spring Boot**: Core framework for building microservices.
**Maven**: Dependency management.
**Spring Security & JWT**: For secure authentication and authorization.
**JUnit, Mockito, PowerMock**: For unit testing.
**Jacoco**: For test coverage.
**Swagger**: API documentation.
**Hibernate & Spring JPA**: ORM and data access layer.
**Spring Cloud:** Microservice infrastructure components like Eureka, Hystrix.
**Databases**: MySQL/PostgreSQL, MongoDB, Cassandra.
**Messaging**: Kafka or RabbitMQ for asynchronous communication.
**Spring Cloud OpenFeign and RestTemplate**: For inter-service communication.


## Test

### Item Service (8080)
`Post http://localhost:8080/api/v1/items`
{
    "name": "Sample Item1",
    "price": 29.99,
    "description": "This is a sample item.",
    "pictureUrls": ["http://example.com/image1.jpg", "http://example.com/image2.jpg"],
    "upc": "123456789012",
    "inventoryCount": 100
}

`Get http://localhost:8080/api/v1/items`

`Get http://localhost:8080/api/v1/items/{id}`
(e.g: http://localhost:8080/api/v1/items/66bfd780028a115849058742)

`Patch http://localhost:8080/api/items/{id}`
(e.g: http://localhost:8080/api/items/66bfd780028a115849058742)
{
    "price": 19.99,
    "inventoryCount": 30
}

`Delete http://localhost:8080/api/items/{id}`
(e.g: )

### Order Service (8000)
`Post http://localhost:8000/api/v1/orders`
{
    "status": "Created",
    "details": "Sample item",
    "amount": 150.00,
    "address": "1234 Street, City, State, Country",
    "items": {
        "66bfd780028a115849058742": 5
    }
}

`Patch http://localhost:8000/api/v1/orders/{id}`
(e.g: http://localhost:8000/api/v1/orders/65292177-46b1-4966-8be5-92384eb581b6)
{
    "address": "1111 Street, City, State, Country"
}

`Get http://localhost:8000/api/v1/orders/{id}`
(e.g: http://localhost:8000/api/v1/orders/65292177-46b1-4966-8be5-92384eb581b6)

`Patch http://localhost:8000/api/v1/orders/{id}/cancel`

### Payment Service (3000)
`Post http://localhost:3000/api/v1/payments`
{
    "orderId": "48c6e10a-109d-4a0e-822c-5f5942bcfe8b",
    "amount": "100"
}

`Patch http://localhost:3000/api/v1/payments/{id}/refund`

`Get http://localhost:3000/api/v1/payments/{id}`

### Account Service (8080) (Just Start)
`Post http://localhost:8080/api/v1/accounts`
{
    "username": "esther2",
    "email": "esther2@example.com",
    "password": "securepassword123",
    "shippingAddress": "1234 Street, City, State, Country",
    "billingAddress": "1234 Street, City, State, Country",
    "paymentMethod": "Credit Card"
}

`Get http://localhost:8080/api/v1/accounts/username/esther1`



### Authentication Service (Not Done yet)
GateWay

### Test (Not Done yet)