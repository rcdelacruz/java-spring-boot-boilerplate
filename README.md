# Spring Boot Training Boilerplate

This boilerplate follows the Java Spring Boot Golden Path and serves as a foundation for training. It includes the standard project structure, key configurations, and example implementations of core components.

## Features

- Spring Boot 3.2.2
- Spring Security with JWT authentication
- Spring Data JPA with PostgreSQL
- Flyway for database migrations
- Swagger UI for API documentation
- Docker and Docker Compose for containerization
- Multi-environment support (development and production)
- CI/CD workflows with GitHub Actions

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

This project supports multiple environments with clear separation between development and production configurations.

### Environment Structure

The project uses the following files for environment configuration:

- **Base Configuration**:
  - `application.yml`: Common settings for all environments
  - `docker-compose.yml`: Base Docker Compose configuration

- **Development Environment**:
  - `application-dev.yml`: Development-specific application settings
  - `docker-compose.dev.yml`: Development-specific Docker settings
  - `.github/workflows/dev-ci.yml`: CI/CD pipeline for the develop branch

- **Production Environment**:
  - `application-prod.yml`: Production-specific application settings
  - `docker-compose.prod.yml`: Production-specific Docker settings
  - `.github/workflows/prod-ci.yml`: CI/CD pipeline for the master branch

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
- Use a named volume for development data persistence

### Production Environment

The production environment is used for production deployments and is associated with the `master` branch.

To start the production environment:

```bash
# Set required environment variables
export JWT_SECRET=your-secure-jwt-secret
export DB_USERNAME=prod-username
export DB_PASSWORD=prod-password
export DB_NAME=prod-db
export CORS_ALLOWED_ORIGINS=https://your-frontend-domain.com

# Start the production environment
docker-compose -f docker-compose.yml -f docker-compose.prod.yml up -d
```

This will:
- Set the active profile to `prod`
- Use production-level logging (INFO)
- Apply production-specific security settings
- Use environment variables for sensitive information
- Apply resource limits for containers
- Use a separate named volume for production data

### Identifying the Current Environment

You can identify which environment the application is running in through multiple methods:

1. **Application Logs**: Check the logs at startup for clear environment indicators:
   ```
   =============================================================
   Application Name: training
   Application Version: 1.0.0
   Build Time: 2025-04-24T08:17:41.173Z
   Active Profile: dev|prod
   Environment: DEVELOPMENT|PRODUCTION
   =============================================================
   ```

2. **Environment Endpoint**: Use the dedicated environment information endpoint:
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

3. **Application Banner**: The application displays a custom banner at startup that includes the active profile.

### CI/CD Workflows

The project includes GitHub Actions workflows for continuous integration and deployment:

#### Development Workflow (`.github/workflows/dev-ci.yml`)

Triggered on:
- Push to the `develop` branch
- Pull requests to the `develop` branch

Steps:
1. Build the application
2. Run unit tests
3. Run integration tests
4. Build the Docker image with development settings
5. Save test results and build artifacts

#### Production Workflow (`.github/workflows/prod-ci.yml`)

Triggered on:
- Push to the `master` branch
- Pull requests to the `master` branch

Steps:
1. Build the application
2. Run unit tests
3. Run integration tests
4. Build the Docker image with production settings
5. Save test results and build artifacts

Both workflows can be extended to include deployment steps to your specific environments.

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
