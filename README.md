# Blog Application with Spring

A comprehensive blog application built with Spring Boot, featuring REST APIs, JWT authentication, and a complete content management system for users, blog posts, and categories.

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technologies](#technologies)
- [Software Requirements](#software-requirements)
- [Installation](#installation)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Project Architecture](#project-architecture)
- [Project Structure](#project-structure)
- [Database Schema](#database-schema)
- [Validation](#validation)
- [Error Handling](#error-handling)
- [Contributing](#contributing)
- [License](#license)

## 🎯 Overview

This is a production-ready blog application built using the Spring Boot ecosystem. It demonstrates modern Java development practices with REST API principles, input validation, comprehensive error handling, and a clean layered architecture separating controllers, services, and repositories.

## ✨ Features

- **User Management**: Create, update, delete, and retrieve user profiles
- **Blog Post Management**: Create, read, update, delete, and search blog posts
- **Category Management**: Organize posts with categories
- **Post Organization**: Links between users, posts, and categories
- **RESTful API**: Clean and consistent REST endpoints with proper HTTP methods
- **Pagination & Filtering**: Retrieve posts with configurable pagination and sorting
- **Post Search**: Search posts by keywords (title-based)
- **Input Validation**: Request validation with field-level error messages
- **Exception Handling**: Comprehensive global exception handling with meaningful error responses
- **DTO Pattern**: Data Transfer Objects to prevent circular references and control API responses
- **Audit Fields**: Automatic tracking of creation and update timestamps

## 🛠️ Technologies

- **Java 21**: Modern Java version with latest features and improvements
- **Spring Boot 4.0.5**: Latest Spring Boot framework
- **Spring Web (MVC)**: For building REST API endpoints
- **Spring Data JPA**: Data access and persistence layer
- **Spring Validation**: Input validation annotations and constraints
- **Spring Security**: Authentication and authorization framework  
- **Hibernate**: ORM framework for database operations
- **MySQL**: Relational database backend
- **JWT (JJWT)**: JSON Web Tokens for stateless authentication
- **ModelMapper**: Automatic DTO and Entity mapping (v3.1.0)
- **Lombok**: Reduces boilerplate code with annotations
- **Jackson**: JSON processing and XML serialization support
- **Spring DevTools**: Hot reload during development
- **Maven**: Build and dependency management
- **JUnit**: Unit testing framework

## 📦 Software Requirements

- **Java Development Kit (JDK)**: Version 21 (required for Spring Boot 4.0.5)
- **Maven**: Version 3.6.0 or higher
- **MySQL**: Version 5.7+ or 8.0+
- **Git**: Version control

## 🚀 Installation

### 1. Clone the Repository

```bash
git clone <repository-url>
cd Blog_Application_With_Spring/demo/demo
```

### 2. Install Dependencies

```bash
mvn clean install
```

### 3. Configure MySQL Database

Open MySQL and create the database:

```sql
CREATE DATABASE blog_app_apis CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 4. Update Application Properties

Edit `src/main/resources/application.properties`:

```properties
# MySQL Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/blog_app_apis
spring.datasource.username=root
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Server Configuration
server.port=9090
spring.application.name=demo
```

**Note**: Replace `your_password` with your actual MySQL root password.

## ⚙️ Configuration

### Application Properties Details

The application configuration in `application.properties` includes:

```properties
# Application Name
spring.application.name=demo

# Server Configuration
server.port=9090                    # Port where the application runs

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/blog_app_apis
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Hibernate/JPA Configuration
spring.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=update    # Auto-create/update schema
spring.jpa.show-sql=true                # Log SQL queries
```

### Audit Fields

All entities automatically track creation and modification:
- `id` - Auto-generated primary key
- `createdAt` - Automatically set on entity creation
- `updatedAt` - Automatically updated on entity modification

### DTO Mapping

The application uses **ModelMapper (v3.1.0)** for automatic conversion between:
- Entities ↔ DTOs (prevents circular references in API responses)
- Maintains clean separation between data models and API contracts

## ▶️ Running the Application

### Using Maven

```bash
cd demo/demo
mvn spring-boot:run
```

### Using IDE

1. Open the project in your IDE (IntelliJ IDEA, Eclipse, VS Code, etc.)
2. Navigate to `DemoApplication.java` located at `src/main/java/com/example/demo/Main/`
3. Right-click and select "Run" or "Run As" → "Java Application"

### Access the Application

The application starts on: **http://localhost:9090**

## 📖 API Documentation

### Base URL
```
http://localhost:9090/api
```

### User Endpoints

| Method | Endpoint | Description | Status |
|--------|----------|-------------|--------|
| POST | `/users/` | Create a new user | ✅ Implemented |
| PUT | `/users/{userId}` | Update user profile | ✅ Implemented |
| DELETE | `/users/{userId}` | Delete a user | ✅ Implemented |
| GET | `/users/{userId}` | Get user by ID | ✅ Implemented |

**Note**: `getAllUsers()` endpoint exists but is not mapped to any HTTP method in the controller.

### Category Endpoints

| Method | Endpoint | Description | Status |
|--------|----------|-------------|--------|
| POST | `/categories/` | Create a new category | ✅ Implemented |
| PUT | `/categories/{catId}` | Update category | ✅ Implemented |
| DELETE | `/categories/{catId}` | Delete category | ✅ Implemented |
| GET | `/categories/{catId}` | Get category by ID | ✅ Implemented |
| GET | `/categories/getAllCategories` | Get all categories | ✅ Implemented |

### Post Endpoints

| Method | Endpoint | Description | Status |
|--------|----------|-------------|--------|
| POST | `/user/{userId}/category/{categoryId}/posts` | Create a new post | ✅ Implemented |
| GET | `/posts` | Get all posts (paginated) | ✅ Implemented |
| GET | `/posts/{postId}` | Get post by ID | ✅ Implemented |
| PUT | `/posts/{postId}` | Update post | ✅ Implemented |
| DELETE | `/posts/{postId}` | Delete post | ✅ Implemented |
| GET | `/user/{userId}/posts` | Get posts by user ID | ✅ Implemented |
| GET | `/category/{categoryId}/posts` | Get posts by category | ✅ Implemented |
| GET | `/posts/search/{keywords}` | Search posts by keywords | ✅ Implemented |

### Query Parameters for Post List

When retrieving all posts, the following parameters are supported:

```
GET /posts?pageNumber=0&pageSize=100&sortBy=updatedAt&sortDir=asc
```

- `pageNumber` (default: 0) - Page number starting from 0
- `pageSize` (default: 100) - Number of posts per page
- `sortBy` (default: updatedAt) - Field to sort by (updatedAt, createdAt, title, etc.)
- `sortDir` (default: asc) - Sort direction (asc or desc)

## 🏗️ Project Architecture

### Layered Architecture

The application follows a classic **three-tier layered architecture**:

```
┌─────────────────────┐
│   Controllers       │  (REST Endpoints)
├─────────────────────┤
│   Services          │  (Business Logic)
├─────────────────────┤
│   Repositories      │  (Data Access)
├─────────────────────┤
│   Entities & DTOs   │  (Models)
└─────────────────────┘
```

### Data Flow

1. **Request** → Controller receives HTTP request
2. **Validation** → Input validation using Jakarta/Jakarta Validation annotations
3. **Transformation** → Controller converts Request DTO to Entity (via ModelMapper)
4. **Processing** → Service implements business logic
5. **Persistence** → Repository performs database operations
6. **Response** → Service converts Entity to Response DTO
7. **Output** → Controller returns HTTP response

### Design Patterns Used

- **DTO Pattern**: Data Transfer Objects prevent circular JSON serialization
- **Repository Pattern**: Data access abstraction through Spring Data JPA
- **Service Layer Pattern**: Business logic separation from controllers
- **ModelMapper Pattern**: Automatic entity-to-DTO mapping
- **Exception Handling Pattern**: Global exception handler for consistent error responses

## 📁 Project Structure

```
Blog_Application_With_Spring/
├── demo/demo/                          # Main application module
│   ├── src/main/java/com/example/demo/
│   │   ├── Main/
│   │   │   └── DemoApplication.java    # Spring Boot entry point
│   │   ├── controller/                 # REST API Controllers
│   │   │   ├── UserController.java
│   │   │   ├── PostController.java
│   │   │   └── CategoryController.java
│   │   ├── services/                   # Business Logic Interface
│   │   │   ├── UserService.java
│   │   │   ├── PostService.java
│   │   │   ├── CategoryService.java
│   │   │   └── impl/                   # Service Implementations
│   │   │       ├── UserServiceImpl.java
│   │   │       ├── PostServiceImpl.java
│   │   │       └── CategoryServiceImpl.java
│   │   ├── repositories/               # Data Access Layer (Spring Data JPA)
│   │   │   ├── UserRepo.java
│   │   │   ├── PostRepo.java
│   │   │   └── CategoryRepo.java
│   │   ├── entities/                   # JPA Entity Classes
│   │   │   ├── User.java
│   │   │   ├── Post.java
│   │   │   ├── Category.java
│   │   │   └── common/                 # Base Entity Classes
│   │   │       ├── IdAuto.java         # ID Generation
│   │   │       ├── CreatedAtAuto.java  # Creation Timestamp
│   │   │       └── SaveAuto.java       # Creation + Update Timestamps
│   │   ├── payloads/                   # API Request/Response Models
│   │   │   ├── ApiResponse.java        # Standard Response Envelope
│   │   │   └── dtos/                   # Data Transfer Objects
│   │   │       ├── UserDto.java
│   │   │       ├── PostDto.java
│   │   │       ├── PostResponse.java   # Paginated Response
│   │   │       └── CategoryDto.java
│   │   ├── exceptions/                 # Exception Handling
│   │   │   ├── ResourceNotFoundException.java
│   │   │   └── GlobalExceptionHandler.java
│   │   └── config/                     # Spring Boot Configuration (empty for now)
│   ├── src/main/resources/
│   │   └── application.properties      # Application Configuration
│   ├── pom.xml                         # Maven Dependencies
│   └── mvnw/mvnw.cmd                   # Maven Wrapper
├── Images/                             # Documentation images and notes
└── README.md                           # This file
```

## 🗄️ Database Schema

### Entity Relationships

```
User (1) ──────────→ (Many) Post
                        ↓ Many
Category (1) ──→ (Many) Post
```

### User Table
```sql
CREATE TABLE user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255),
    password VARCHAR(255),
    about TEXT,
    created_at DATETIME,
    updated_at DATETIME
);
```

### Category Table
```sql
CREATE TABLE category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    category_title VARCHAR(100) NOT NULL,
    category_description VARCHAR(255),
    created_at DATETIME,
    updated_at DATETIME
);
```

### Post Table
```sql
CREATE TABLE post (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    content VARCHAR(10000),
    image_name VARCHAR(255),
    add_date DATE,
    category_id BIGINT,
    user_id BIGINT,
    created_at DATETIME,
    updated_at DATETIME,
    FOREIGN KEY (category_id) REFERENCES category(id),
    FOREIGN KEY (user_id) REFERENCES user(id)
);
```

## ✅ Validation

All input validation is handled at the DTO level using Jakarta Validation annotations:

### User DTO Validation
- `name` - Required (not empty)
- `email` - Valid email format, minimum 4 characters
- `password` - Required, between 3-30 characters
- `about` - Required (not null/empty)

### Category DTO Validation
- `categoryTitle` - Required, between 4-200 characters
- `categoryDescription` - Required, between 4-200 characters

### PostDto
- No validation annotations (can be enhanced)

**Note**: Validation errors return appropriate HTTP 400 responses with field-level error messages.

## 🚨 Error Handling

### Exception Handling Strategy

All exceptions are handled by the **GlobalExceptionHandler** class:

#### Handled Exceptions

1. **ResourceNotFoundException**
   - Thrown when: Entity is not found in database
   - Response: HTTP 404 with ApiResponse containing error message
   - Example: User with ID 999 doesn't exist

2. **MethodArgumentNotValidException**
   - Thrown when: Request validation fails
   - Response: HTTP 400 with field-level error messages
   - Example: Invalid email format or missing required fields

### Response Format

#### Success Response
```json
{
    "message": "Operation successful",
    "success": true
}
```

#### Error Response
```json
{
    "message": "User not found with id: 999",
    "success": false
}
```

#### Validation Error Response
```json
{
    "email": "given email address isn't valid",
    "password": "Password must be between 3 and 30 characters.",
    "about": "must not be empty"
}
```
## 🔧 Testing

The application includes a test class at `src/main/java/com/example/demo/test/ApplicationTests.java` for unit testing. To run tests:

```bash
mvn test
```

## 📝 Known Limitations & Future Enhancements

### Current Limitations
- **No Authentication Endpoints**: Register and login endpoints are not implemented
- **No Security Configuration**: While Spring Security is imported, no security configuration is in place
- **Inactive Method**: `UserController.getAllUsers()` is not mapped to any HTTP method
- **Search Optimization**: Post search uses simple title matching; consider implementing full-text search for production
- **Missing Comments**: While mentioned in requirements, comments/replies system is not implemented
- **No Image Upload**: Image handling (imageName field) is placeholder-only
- **No Pagination for Categories**: Category list is not paginated

### Recommended Enhancements
1. **Authentication & Authorization**
   - Implement JWT token generation and validation
   - Create login/register endpoints
   - Add role-based access control (Admin, User)

2. **Advanced Search**
   - Implement full-text search in MySQL
   - Add filters by date range, category, author

3. **Comments System**
   - Create Comment entity and endpoints
   - Add reply-to-comment functionality

4. **File Upload**
   - Implement image upload/download for posts
   - Add file storage configuration

5. **Pagination**
   - Add pagination to all list endpoints
   - Include pagination to category listing

6. **Performance**
   - Add query optimization and indexes
   - Implement caching (Redis)
   - Add API rate limiting

7. **Testing**
   - Add integration tests
   - Add controller tests with MockMvc
   - Add service layer tests

## 🛡️ Security Notes

The project imports Spring Security but doesn't include security configuration. When implementing authentication:

- Use BCrypt for password hashing (never store plain passwords)
- Implement JWT for stateless authentication
- Add CORS configuration if building a frontend
- Implement rate limiting for API endpoints
- Use HTTPS in production
- Validate and sanitize all user inputs

## 📚 Example Usage

### Create a User
```bash
curl -X POST http://localhost:9090/api/users/ \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "password123",
    "about": "A passionate blogger"
  }'
```

### Create a Category
```bash
curl -X POST http://localhost:9090/api/categories/ \
  -H "Content-Type: application/json" \
  -d '{
    "categoryTitle": "Technology",
    "categoryDescription": "Tech related blog posts"
  }'
```

### Create a Post
```bash
curl -X POST http://localhost:9090/api/user/1/category/1/posts \
  -H "Content-Type: application/json" \
  -d '{
    "title": "My First Blog Post",
    "content": "This is the content of my first blog post.",
    "imageName": "post-image.jpg"
  }'
```

### Get All Posts with Pagination
```bash
curl "http://localhost:9090/api/posts?pageNumber=0&pageSize=10&sortBy=updatedAt&sortDir=desc"
```

### Search Posts
```bash
curl "http://localhost:9090/api/posts/search/technology"
```

## 🤝 Contributing

This is an educational project. Contributions are welcome! To contribute:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/YourFeature`)
3. Make your improvements
4. Commit your changes (`git commit -m 'Add YourFeature'`)
5. Push to the branch (`git push origin feature/YourFeature`)
6. Open a Pull Request

### Contribution Guidelines
- Write clear commit messages
- Add tests for new functionality
- Follow existing code style and patterns
- Update README if adding new features
- Ensure all tests pass before submitting PR

## 📄 License

This project is licensed under the MIT License. See the LICENSE file for details.

---

**Project Status**: This is an educational Spring Boot project demonstrating modern Java web development practices. It's suitable for learning purposes and can be extended with additional features for production use.

**Last Updated**: 2026