# Employee Management API

A modern Spring Boot RESTful API for employee management built with Java 25, Spring Boot 3.4.2, Spring Data JPA, and H2 Database.

## Features

- **RESTful Endpoints**: Full CRUD operations for employee resources.
- **Spring Data JPA & H2**: In-memory database with automatic schema generation and H2 web console access.
- **Constructor Injection**: Clean, testable, and immutable dependency injection pattern.
- **Unit & Integration Testing**: 19 test cases covering controllers, services, and repositories using JUnit 5, Mockito, and Spring `@DataJpaTest`.
- **CORS Configured**: Pre-configured for cross-origin frontend requests.

---

## Tech Stack

| Technology | Version / Tool |
| :--- | :--- |
| **Java** | 25 (Compiler Target 21) |
| **Framework** | Spring Boot 3.4.2 |
| **Persistence** | Spring Data JPA / Hibernate |
| **Database** | H2 In-Memory Database |
| **Build Tool** | Apache Maven 3.9+ |
| **Testing** | JUnit 5, Mockito, Spring MockMvc |

---

## Project Structure

```text
employee-management-api/
├── pom.xml
├── README.md
└── src/
    ├── main/
    │   ├── java/net/dixonai/employeemanagement/
    │   │   ├── EmployeeManagementApiApplication.java
    │   │   ├── controller/
    │   │   │   ├── EmployeeController.java
    │   │   │   └── WelcomeController.java
    │   │   ├── model/
    │   │   │   └── Employee.java
    │   │   ├── repository/
    │   │   │   └── EmployeeRepository.java
    │   │   └── service/
    │   │       └── EmployeeService.java
    │   └── resources/
    │       └── application.properties
    └── test/
        ├── java/net/dixonai/employeemanagement/
        │   ├── controller/
        │   │   ├── EmployeeControllerTest.java
        │   │   └── WelcomeControllerTest.java
        │   ├── repository/
        │   │   └── EmployeeRepositoryTest.java
        │   └── service/
        │       └── EmployeeServiceTest.java
        └── resources/
            ├── application.properties
            └── mockito-extensions/
                └── org.mockito.plugins.MockMaker
```

---

## Getting Started

### Prerequisites

- **Java Development Kit (JDK)**: Java 21 or Java 25+
- **Maven**: 3.8+

### Installation & Setup

1. **Navigate to the API directory**:
   ```bash
   cd employee-management-api
   ```

2. **Compile the project**:
   ```bash
   mvn clean compile
   ```

3. **Run tests**:
   ```bash
   mvn test
   ```

4. **Run the Application**:
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`.

---

## API Endpoints

### 1. System Health / Welcome Endpoint

| Method | Endpoint | Description | Sample Response |
| :--- | :--- | :--- | :--- |
| `GET` | `/` | Returns API status message | `"Employee Management API is up and running ..."` |

### 2. Employee Resource Endpoints

| Method | Endpoint | Description | Request Body | Response Status |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/employees` | Retrieve all employees | None | `200 OK` |
| `POST` | `/employees` | Create a new employee | JSON Payload | `200 OK` |
| `GET` | `/employees/{id}` | Retrieve employee by ID | None | `200 OK` / `404 Not Found` |
| `PUT` | `/employees/{id}` | Update employee details | JSON Payload | `200 OK` / `404 Not Found` |
| `DELETE` | `/employees/{id}` | Delete employee by ID | None | `204 No Content` |

---

## Sample Request Payloads

### Create Employee (`POST /employees`)
```json
{
  "id": "emp-101",
  "firstName": "John",
  "lastName": "Doe",
  "emailId": "john.doe@example.com",
  "department": "Engineering"
}
```

### Update Employee (`PUT /employees/emp-101`)
```json
{
  "firstName": "Johnathan",
  "lastName": "Doe",
  "emailId": "johnathan.doe@example.com",
  "department": "Product Engineering"
}
```

---

## Database Access (H2 Console)

An in-memory H2 database console is enabled for development:

- **URL**: `http://localhost:8080/h2-console`
- **JDBC URL**: `jdbc:h2:mem:employeedb`
- **User Name**: `sa`
- **Password**: *(leave blank)*

---

## Testing

Run all 19 unit and integration tests using Maven:
```bash
mvn test
```

### Test Coverage Summary:
- **`EmployeeServiceTest`**: Unit tests for business logic and exception handling.
- **`EmployeeControllerTest`**: MockMvc controller tests for HTTP status codes and JSON responses.
- **`WelcomeControllerTest`**: Unit test for root status endpoint.
- **`EmployeeRepositoryTest`**: `@DataJpaTest` integration tests against H2 database.

---

## License

This project is licensed under the MIT License.
