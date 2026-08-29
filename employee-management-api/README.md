# Employee Management API

A modern Spring Boot RESTful API for employee management built with Java 25, Spring Boot 3.4.2, Spring Security, JWT Authentication, Spring Data MongoDB (Runtime), and Nitrite Embedded NoSQL Database (Automated Testing).

## Features

- **Spring Security & JWT Authentication**:
  - Stateless API security using a custom `SecurityFilterChain` and JWT token authentication.
  - `POST /auth/login` endpoint for employee authentication issuing signed JWT tokens (`Bearer`).
  - Protected `/employees/**` endpoints requiring a valid `Authorization: Bearer <token>` header.
- **BCrypt Password Hashing**: Adaptive, salted one-way password hashing using Spring Security's `BCryptPasswordEncoder`.
- **RESTful Endpoints**: Full CRUD operations for employee resources (`GET`, `POST`, `PUT`, `DELETE`).
- **Dual Database Persistence Architecture**:
  - **Runtime Application**: Uses **Spring Data MongoDB** connecting dynamically via `MONGODB_URI` from `.env`.
  - **Automated Tests**: Uses **Nitrite Embedded NoSQL Database**, ensuring fast, isolated testing without requiring an active database server or SQL/JPA setup.
- **Auto UUID Generation**: Automatically generates a unique UUID string when creating new employees if an ID is not supplied.
- **Global Exception Handling**: Graceful exception handling for database connection timeouts (`MongoException`) returning friendly `503 Service Unavailable` JSON responses.
- **Comprehensive Testing**: 40 test cases covering security, controllers, authentication, services, and repositories using JUnit 5, Mockito, and Nitrite repository testing.
- **CORS Configured**: Pre-configured for cross-origin frontend requests.

---

## Tech Stack

| Technology | Version / Tool | Purpose |
| :--- | :--- | :--- |
| **Java** | 25 | Core Programming Language |
| **Framework** | Spring Boot 3.4.2 | Backend Framework |
| **Security** | Spring Security & JJWT 0.12.6 | Authentication & Token Management |
| **Password Hashing** | BCrypt (`BCryptPasswordEncoder`) | Strong One-Way Password Hashing |
| **Persistence (Runtime)** | Spring Data MongoDB | Production & Dev NoSQL Data Layer |
| **Persistence (Testing)** | Nitrite NoSQL Database (3.4.4) | Isolated In-Memory Embedded NoSQL Test Database |
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
    │   │   │   ├── DataConfig.java
    │   │   │   └── SecurityConfig.java
    │   │   ├── controller/
    │   │   │   ├── AuthController.java
    │   │   │   ├── EmployeeController.java
    │   │   │   └── WelcomeController.java
    │   │   ├── dto/
    │   │   │   ├── LoginRequest.java
    │   │   │   └── LoginResponse.java
    │   │   ├── exception/
    │   │   │   └── GlobalExceptionHandler.java
    │   │   ├── model/
    │   │   │   └── Employee.java
    │   │   ├── repository/
    │   │   │   ├── EmployeeRepository.java (Common Interface)
    │   │   │   ├── NitriteEmployeeRepository.java (Test Profile)
    │   │   │   └── MongoEmployeeRepository.java (App Profile)
    │   │   ├── security/
    │   │   │   ├── JwtAuthenticationFilter.java
    │   │   │   └── JwtTokenProvider.java
    │   │   └── service/
    │   │       ├── EmployeeService.java
    │   │       └── PasswordEncryptionService.java
    │   └── resources/
    │       └── application.properties
    └── test/
        ├── java/net/dixonai/employeemanagement/
        │   ├── controller/
        │   │   ├── AuthControllerTest.java
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

Copy `.env-example` to create your local `.env` file in `employee-management-api/`:

```bash
cp .env-example .env
```

Environment variables configured in `.env`:

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

### 1. Public Endpoints

| Method | Endpoint | Description | Request Body | Response Status |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/` | Returns API status message | None | `200 OK` |
| `POST` | `/auth/login` | Authenticate & receive JWT token | `{ "emailId", "password" }` | `200 OK` / `401 Unauthorized` |

### 2. Protected Employee Endpoints (`Authorization: Bearer <token>`)

| Method | Endpoint | Description | Request Body | Response Status |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/employees` | Retrieve all employees | None | `200 OK` / `401 Unauthorized` |
| `POST` | `/employees` | Create a new employee | JSON Payload | `200 OK` / `401 Unauthorized` |
| `GET` | `/employees/{id}` | Retrieve employee by ID | None | `200 OK` / `404 Not Found` |
| `PUT` | `/employees/{id}` | Update employee details | JSON Payload | `200 OK` / `404 Not Found` |
| `DELETE` | `/employees/{id}` | Delete employee by ID | None | `204 No Content` |

---

## Sample Request Payloads

### System Login (`POST /auth/login`)
```json
{
  "emailId": "john.doe@example.com",
  "password": "mySecurePassword123"
}
```

### Create Employee with System Login (`POST /employees`)
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "emailId": "john.doe@example.com",
  "department": "Engineering",
  "loginEnabled": true,
  "password": "mySecurePassword123"
}
```

*Note: Passwords are automatically hashed using BCrypt before saving and masked in response outputs.*

---

## Testing

Run all 40 unit and integration tests using Maven:
```bash
mvn clean test
```

### Test Coverage Summary:
- **`JwtTokenProviderTest`**: Tests JWT token generation, claim payload validation, and expiration parsing.
- **`JwtAuthenticationFilterTest`**: Tests HTTP Authorization header extraction and SecurityContext authentication.
- **`AuthControllerTest`**: Verifies login authentication, JWT token issuance, invalid passwords, and disabled login accounts.
- **`EmployeeServiceTest`**: Unit tests for business logic, CRUD, BCrypt password hashing, and automatic UUID generation.
- **`PasswordEncryptionServiceTest`**: Tests BCrypt password hashing and password matching.
- **`GlobalExceptionHandlerTest`**: Tests MongoException error handling and 503 Service Unavailable responses.
- **`EmployeeControllerTest`**: MockMvc controller tests for HTTP status codes and JSON responses.
- **`WelcomeControllerTest`**: Unit test for root status endpoint.
- **`EmployeeRepositoryTest`**: Integration tests against Nitrite in-memory embedded NoSQL database using `@SpringBootTest` and `@ActiveProfiles("test")`.


---

## License

This project is licensed under the MIT License.

