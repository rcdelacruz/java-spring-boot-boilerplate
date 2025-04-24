# Spring Boot Training Boilerplate

This boilerplate follows the Java Spring Boot Golden Path and serves as a foundation for training. It includes the standard project structure, key configurations, and example implementations of core components.

## Project Structure

```
src/main/java/com/example/training/
├── config/                 # Configuration classes
│   ├── SecurityConfig.java
│   ├── WebConfig.java
│   └── SwaggerConfig.java
├── controller/             # REST controllers
│   ├── UserController.java
│   └── AuthController.java
├── dto/                    # Data transfer objects
│   ├── UserDto.java
│   ├── AuthRequestDto.java
│   └── AuthResponseDto.java
├── exception/              # Custom exceptions and handler
│   ├── GlobalExceptionHandler.java
│   ├── ResourceNotFoundException.java
│   └── ValidationException.java
├── model/                  # Domain entities
│   └── User.java
├── repository/             # Data access layer
│   └── UserRepository.java
├── security/               # Security configuration
│   ├── JwtAuthenticationFilter.java
│   ├── JwtTokenProvider.java
│   └── CustomUserDetailsService.java
├── service/                # Business logic
│   ├── UserService.java
│   ├── AuthService.java
│   └── impl/
│       ├── UserServiceImpl.java
│       └── AuthServiceImpl.java
├── util/                   # Utility classes
│   └── AppUtils.java
└── TrainingApplication.java  # Application entry point
```

## Key Features

- Layered architecture following Spring Boot best practices
- JWT-based authentication and authorization
- User management with profile retrieval
- Database migration with Flyway
- Comprehensive exception handling
- API documentation with OpenAPI/Swagger
- Actuator endpoints for monitoring
- Unit and integration testing
- Docker and Docker Compose configuration
- CI/CD pipeline with GitHub Actions

## Quick Start

1. Clone the repository
2. Navigate to the project directory
3. Start the application with Docker Compose:
   ```bash
   docker-compose up -d
   ```
4. Access the API documentation at: http://localhost:8080/swagger-ui.html
5. Check the application health at: http://localhost:8080/actuator/health

## API Endpoints

### Authentication
- `POST /api/v1/auth/register` - Register a new user
- `POST /api/v1/auth/login` - Login and get JWT token

### User Management
- `GET /api/v1/users` - Get all users (admin only)
- `GET /api/v1/users/{id}` - Get user by ID
- `GET /api/v1/users/me` - Get current user profile
- `PUT /api/v1/users/{id}` - Update user
- `DELETE /api/v1/users/{id}` - Delete user

## User Roles

The application supports the following user roles:

- `USER` - Regular user with standard permissions
- `ADMIN` - Administrator with full access to all features

When registering a new user, you can specify the roles in the request body:
```json
{
  "username": "newuser",
  "email": "user@example.com",
  "password": "password123",
  "firstName": "New",
  "lastName": "User",
  "roles": ["USER"]
}
```

If no roles are specified during registration, the default role `USER` will be assigned.

## Development Flow

1. Build the application:
   ```bash
   ./mvnw clean package
   ```
2. Run tests:
   ```bash
   ./mvnw test
   ```
3. Run integration tests:
   ```bash
   ./mvnw verify -P integration-test
   ```
4. Start the application in development mode:
   ```bash
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=local
   ```

## Authentication Example

### Register a new user
```bash
curl -X POST "http://localhost:8080/api/v1/auth/register" \
  -H "Content-Type: application/json" \
  -d '{"username":"newuser","email":"user@example.com","password":"password123","firstName":"New","lastName":"User","roles":["USER"]}'
```

### Login and get JWT token
```bash
curl -X POST "http://localhost:8080/api/v1/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"username":"newuser","password":"password123"}'
```

### Get current user profile
```bash
curl -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  http://localhost:8080/api/v1/users/me
```
