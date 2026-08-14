# Employee Management API

A modern Spring Boot RESTful API for employee management built with Java 25, Spring Boot 3.4.2, Spring Data MongoDB (Runtime), and H2 In-Memory Database (Automated Testing).

## Features

- **RESTful Endpoints**: Full CRUD operations for employee resources (`GET`, `POST`, `PUT`, `DELETE`).
- **Dual Database Persistence Architecture**:
  - **Runtime Application**: Uses **Spring Data MongoDB** connecting dynamically via `MONGODB_URI` from `.env`.
  - **Automated Tests**: Uses **Spring Data JPA & H2 In-Memory Database** (`jdbc:h2:mem:employeedb`), ensuring fast, isolated testing without requiring an active database server.
- **Auto UUID Generation**: Automatically generates a unique UUID string when creating new employees if an ID is not supplied.
- **Global Exception Handling**: Graceful exception handling for database connection timeouts (`MongoException`) returning friendly `503 Service Unavailable` JSON responses.
- **Comprehensive Testing**: 20 test cases covering controllers, services, and repositories using JUnit 5, Mockito, and Spring `@DataJpaTest`.
- **CORS Configured**: Pre-configured for cross-origin frontend requests.

---

## Tech Stack

| Technology | Version / Tool | Purpose |
| :--- | :--- | :--- |
| **Java** | 25 (Compiler Target 21) | Core Programming Language |
| **Framework** | Spring Boot 3.4.2 | Backend Framework |
| **Persistence (Runtime)** | Spring Data MongoDB | Production & Dev NoSQL Data Layer |
| **Persistence (Testing)** | Spring Data JPA / H2 | Isolated In-Memory SQL Test Database |
| **Build Tool** | Apache Maven 3.9+ | Dependency & Build Management |
| **Testing** | JUnit 5, Mockito, MockMvc | Automated Testing Suite |

---

## Project Structure

```text
employee-management-api/
├── .env
├── .env-example
├── pom.xml
├── README.md
└── src/
    ├── main/
    │   ├── java/net/dixonai/employeemanagement/
    │   │   ├── EmployeeManagementApiApplication.java
    │   │   ├── config/
    │   │   │   └── DataConfig.java
    │   │   ├── controller/
    │   │   │   ├── EmployeeController.java
    │   │   │   └── WelcomeController.java
    │   │   ├── exception/
    │   │   │   └── GlobalExceptionHandler.java
    │   │   ├── model/
    │   │   │   └── Employee.java
    │   │   ├── repository/
    │   │   │   ├── EmployeeRepository.java (Common Interface)
    │   │   │   ├── JpaEmployeeRepository.java (Test Profile)
    │   │   │   └── MongoEmployeeRepository.java (App Profile)
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
            └── application.properties
```

---

## Getting Started

### Prerequisites

- **Java Development Kit (JDK)**: Java 21 or Java 25+
- **Maven**: 3.8+
- **MongoDB** (Cloud Atlas or Local Instance for normal app runtime)

### Environment Setup

Create a `.env` file in `employee-management-api/`:

```env
MONGODB_URI=mongodb+srv://<username>:<password>@cluster.mongodb.net/employee-management?retryWrites=true&w=majority&serverSelectionTimeoutMS=5000
PORT=8080
```

### Installation & Execution

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
   mvn clean test
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
  "firstName": "John",
  "lastName": "Doe",
  "emailId": "john.doe@example.com",
  "department": "Engineering"
}
```

*Note: If `id` is omitted, the API automatically generates a UUID.*

### Update Employee (`PUT /employees/{id}`)
```json
{
  "firstName": "Johnathan",
  "lastName": "Doe",
  "emailId": "johnathan.doe@example.com",
  "department": "Product Engineering"
}
```

---

## Testing

Run all 20 unit and integration tests using Maven:
```bash
mvn clean test
```

### Test Coverage Summary:
- **`EmployeeServiceTest`**: Unit tests for business logic, CRUD, and automatic UUID generation.
- **`EmployeeControllerTest`**: MockMvc controller tests for HTTP status codes and JSON responses.
- **`WelcomeControllerTest`**: Unit test for root status endpoint.
- **`EmployeeRepositoryTest`**: Integration tests against H2 in-memory database using `@DataJpaTest` and `@ActiveProfiles("test")`.

---

## License

This project is licensed under the MIT License.
