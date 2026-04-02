# Blog Application with Spring

A full-featured blog application built with Spring Boot, featuring REST APIs, security, and real-time messaging capabilities using Kafka.

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technologies](#technologies)
- [Software Requirements](#software-requirements)
- [Installation](#installation)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Project Structure](#project-structure)
- [Contributing](#contributing)
- [License](#license)

## 🎯 Overview

This is a comprehensive blog application built using the Spring ecosystem. It demonstrates modern Java development practices with REST API principles, security best practices, and event-driven architecture using Kafka for asynchronous messaging.

## ✨ Features

- **User Management**: Create, authenticate, and manage user accounts
- **Blog Post Management**: Create, read, update, and delete blog posts
- **Comments System**: Add comments to blog posts
- **User Authentication & Authorization**: Secure endpoints with Spring Security
- **Role-Based Access Control**: Admin and user roles with different permissions
- **RESTful API**: Clean and well-structured REST endpoints
- **Event-Driven Architecture**: Real-time event processing using Kafka
- **Database Persistence**: JPA/Hibernate with Spring Data JPA
- **Input Validation**: Request validation and error handling
- **Pagination & Filtering**: Efficient data retrieval and sorting

## 🛠️ Technologies

- **Java 11+**: Programming language
- **Spring Boot**: Framework for building applications
- **Spring Web (Spring MVC)**: REST API development
- **Spring Security**: Authentication and authorization
- **Spring Data JPA**: Data access layer
- **Kafka**: Event streaming and asynchronous messaging
- **Hibernate**: ORM framework
- **MySQL/PostgreSQL**: Relational database
- **Maven**: Dependency management and build tool
- **Lombok**: Reduce boilerplate code
- **Jackson**: JSON processing
- **JWT**: JSON Web Tokens for stateless authentication

## 📦 Software Requirements

- **Java Development Kit (JDK)**: Version 11 or higher
- **Maven**: Version 3.6.0 or higher
- **MySQL** or **PostgreSQL**: Database server
- **Kafka**: Message broker (optional for local development)
- **Git**: Version control

## 🚀 Installation

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/Blog_Application_With_Spring.git
cd Blog_Application_With_Spring
```

### 2. Install Dependencies

```bash
mvn clean install
```

### 3. Configure Database

Update `application.properties` or `application.yml` with your database configuration:

```properties
# MySQL Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/blog_db
spring.datasource.username=root
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

### 4. Configure Kafka (Optional)

```properties
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.producer.value-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.consumer.value-deserializer=org.apache.kafka.common.serialization.StringDeserializer
```

### 5. Create Database Schema

```sql
CREATE DATABASE blog_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

## ⚙️ Configuration

### Spring Security Configuration

- User registration and login endpoints are public
- All blog endpoints require authentication
- Admin-only endpoints require ADMIN role
- Password encryption using BCrypt

### Application Properties

Key properties in `application.properties`:

```properties
# Server Configuration
server.port=8080

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/blog_db
spring.datasource.username=root
spring.datasource.password=password

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true

# JWT Configuration
app.jwtSecret=your_jwt_secret_key
app.jwtExpirationMs=86400000
```

## ▶️ Running the Application

### Using Maven

```bash
mvn spring-boot:run
```

### Using IDE

1. Open the project in your IDE (IntelliJ IDEA, Eclipse, VS Code, etc.)
2. Right-click on the main class (`BlogApplication.java`)
3. Select "Run As" → "Java Application"

The application will start on `http://localhost:8080`

## 📖 API Documentation

### Base URL
```
http://localhost:8080/api/v1
```

### User Endpoints

- `POST /auth/register` - Register a new user
- `POST /auth/login` - Login user
- `GET /users/{id}` - Get user by ID
- `PUT /users/{id}` - Update user profile

### Blog Post Endpoints

- `GET /posts` - Get all posts (paginated)
- `GET /posts/{id}` - Get post by ID
- `POST /posts` - Create new post (authenticated)
- `PUT /posts/{id}` - Update post (author only)
- `DELETE /posts/{id}` - Delete post (author or admin)
- `GET /posts/search?keyword=...` - Search posts

### Comment Endpoints

- `GET /posts/{id}/comments` - Get comments for a post
- `POST /posts/{id}/comments` - Add comment to a post
- `DELETE /comments/{id}` - Delete comment (author or admin)

## 📁 Project Structure

```
Blog_Application_With_Spring/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/blog/
│   │   │       ├── controller/       # REST controllers
│   │   │       ├── service/          # Business logic
│   │   │       ├── repository/       # Data access layer
│   │   │       ├── entity/           # JPA entities
│   │   │       ├── dto/              # Data transfer objects
│   │   │       ├── security/         # Security configuration
│   │   │       ├── exception/        # Custom exceptions
│   │   │       └── BlogApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── application-dev.properties
│   └── test/                         # Unit and integration tests
├── pom.xml
└── README.md
```

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

---

**Note**: This is a learning project based on the Spring Boot tutorial series. Feel free to contribute and improve!