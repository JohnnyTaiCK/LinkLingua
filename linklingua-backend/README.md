# LinkLingua Backend

LinkLingua backend service.

## Tech Stack

| Technology | Version |
| --- | --- |
| Spring Boot | 3.2.5 |
| Java | 21 |
| MyBatis (mybatis-spring-boot-starter) | 3.0.3 |
| MySQL | 8.0 |
| SpringDoc OpenAPI (Swagger) | 2.5.0 |
| Lombok | Managed by Spring Boot |
| Spring Validation | Managed by Spring Boot |

## Package Structure

```
com.linklingua
├── common       // Common classes (unified Result, ResultCode, business exception, global exception handler)
├── config       // Configuration classes (OpenApiConfig, MyBatisConfig)
├── controller   // Controller layer
├── dto          // Request data transfer objects
├── vo           // Response view objects
├── entity       // Database entity classes
├── mapper       // MyBatis Mapper interfaces
├── service      // Business logic layer
│   └── impl     // Business implementation classes
└── util         // Utility classes
```

## Quick Start

### 1. Prepare the database

Make sure MySQL 8.0 is installed and running locally, then run the initialization script:

```bash
mysql -u root -p < src/main/resources/sql/schema.sql
```

### 2. Update the database connection

Update the username and password in `src/main/resources/application.properties` as needed:

```properties
spring.datasource.username=root
spring.datasource.password=root
```

### 3. Run the project

```bash
# Run with Maven
mvn spring-boot:run

# Or package first and then run
mvn clean package
java -jar target/linklingua-backend-1.0.0.jar
```

> Requires JDK 21 and Maven 3.6+ installed locally.

### 4. Access the API documentation

After startup, open the Swagger UI:

- Swagger UI: <http://localhost:8080/swagger-ui.html>
- OpenAPI JSON: <http://localhost:8080/v3/api-docs>

## REST Endpoints

All resources follow the same RESTful CRUD convention:

| Method | Path | Description |
| --- | --- | --- |
| POST | `/api/{resource}` | Create |
| PUT | `/api/{resource}/{id}` | Update by id |
| DELETE | `/api/{resource}/{id}` | Delete by id |
| GET | `/api/{resource}/{id}` | Get by id |
| GET | `/api/{resource}` | List |

Available resources: `cities`, `language-tags`, `users`, `events`.

The events list additionally supports optional query filters:

```
GET /api/events?cityId=1&langId=3&eventStatus=1
```

### Unified Response Structure

```json
{
  "code": 200,
  "message": "Operation succeeded",
  "data": {}
}
```
