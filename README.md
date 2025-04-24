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
- `ADMIN` - Administrator with full access to all features and endpoints

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

## Branching Strategy

This project follows the Git Flow branching strategy:

- **`develop`** - Default branch for development work. All feature branches are created from and merged back into this branch.
- **`master`** - Production branch that contains stable, release-ready code.
- **Feature branches** - Created from `develop` for new features or changes (e.g., `feature/add-new-endpoint`).
- **Hotfix branches** - Created from `master` for urgent fixes to production (e.g., `hotfix/fix-critical-bug`).

## Environment Setup

This project supports multiple environments:

### Development Environment

The development environment is used for ongoing development work and is associated with the `develop` branch.

To start the development environment:

```bash
docker-compose -f docker-compose.yml -f docker-compose.dev.yml up -d
```

This will:
- Set the active profile to `dev`
- Enable debug logging
- Use development-specific settings

### Production Environment

The production environment is used for production deployments and is associated with the `master` branch.

To start the production environment:

```bash
# Set required environment variables
export JWT_SECRET=your-secure-jwt-secret
export DB_USERNAME=prod-username
export DB_PASSWORD=prod-password
export DB_NAME=prod-db

# Start the production environment
docker-compose -f docker-compose.yml -f docker-compose.prod.yml up -d
```

This will:
- Set the active profile to `prod`
- Use production-level logging (INFO)
- Apply production-specific security settings
- Use environment variables for sensitive information

### Identifying the Current Environment

You can identify which environment the application is running in by:

1. Checking the logs at startup:
   ```
   Application Name: training
   Application Version: 1.0.0
   Active Profile: dev|prod
   Environment: DEVELOPMENT|PRODUCTION
   ```

2. Using the environment endpoint:
   ```bash
   curl http://localhost:8080/api/v1/environment
   ```

   Response:
   ```json
   {
     "activeProfile": "dev|prod",
     "environment": "Development|Production",
     "buildTime": "...",
     "version": "1.0.0"
   }
   ```

### Workflow

1. Create a feature branch from `develop`:
   ```bash
   git checkout develop
   git pull
   git checkout -b feature/your-feature-name
   ```

2. Make changes, commit, and push to the feature branch:
   ```bash
   git add .
   git commit -m "Implement feature X"
   git push -u origin feature/your-feature-name
   ```

3. Create a Pull Request (PR) from your feature branch to `develop`.

4. After review and approval, merge the PR into `develop`.

5. When ready for release, create a PR from `develop` to `master`.

6. After testing and approval, merge the PR into `master` for production deployment.

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
