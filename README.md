# 📚 Book Store Management System

A modern full-stack application for managing a digital bookstore, built with Spring Boot and React. This system provides a comprehensive solution for book inventory management, user authentication, and order processing.

# 🌐 Domain
E-commerce / Retail / Book Management

# 🎯 Objectives

- Manage book inventory with detailed information
- Handle user authentication and authorization
- Process and track book orders
- Provide an intuitive admin interface
- Offer a responsive user experience

# 🧱 Tech Stack
Layer | Technology
------|------------
Frontend | React + TailwindCSS
Backend | Spring Boot 3.1.5
Security | Spring Security + JWT
Persistence | Spring Data JPA
Database | MySQL
Build Tools | Maven (Backend), npm (Frontend)
Documentation | Swagger (springdoc-openapi)
Logging | Log4j2

# 🧩 Key Modules

- User Authentication & Authorization
- Book Inventory Management
- Order Processing
- Admin Dashboard
- User Profile Management

# 🔐 Roles & Access
Role | Access Description
-----|-------------------
Admin | Full access to manage books, users, and orders
User | Browse books, manage profile, place orders
Vendor | Manage their own book inventory and view sales
Guest | View books and public information

# 🗃 Entity Overview

- User: id, username, email, password, role
- Book: id, title, author, price, category, stock
- Order: id, userId, status, orderDate
- OrderItem: id, orderId, bookId, quantity

# 🔁 REST API Endpoints

## 🔐 AuthController

- POST /api/auth/register
- POST /api/auth/login

## 📚 BookController

- GET /api/books
- GET /api/books/{id}
- POST /api/books (Admin)
- PUT /api/books/{id} (Admin)
- DELETE /api/books/{id} (Admin)

## 👤 UserController

- GET /api/users/profile
- PUT /api/users/profile
- GET /api/users (Admin)

## 🛒 OrderController

- POST /api/orders
- GET /api/orders
- GET /api/orders/{id}
- PUT /api/orders/{id}/status (Admin)

# 📁 Project Structure

```
com.bookstore.bookstore_app
├── config/
├── controller/
├── dto/
├── entity/
├── exception/
├── repository/
├── service/
└── BookstoreAppApplication.java

frontend/
├── src/
│   ├── components/
│   ├── pages/
│   ├── services/
│   └── App.js
└── package.json
```

# 📊 Database Design

![ER Diagram](assets/ER%20Diagram.jpeg)

# 📝 Class Diagram

![Class Diagram](assets/Class%20Diagram.jpeg)

# 📘 Swagger Documentation

![Swagger Documentation 1](assets/swagger1.jpeg)

![Swagger Documentation 2](assets/swagger2.jpeg)

![Swagger Documentation 3](assets/swagger3.jpeg)

# ▶️ How to Use the Project

## 🛠 Prerequisites

- Java 17+
- Springboot
- Node.js and npm
- MySQL database
- Maven

## 🚀 Steps to Run

1. Clone the Repository

```bash
git clone <repository-url>
cd book-store
```

2. Set up the Database

- Create a MySQL database
- Update application.properties with your database credentials

3. Run the Backend

```bash
cd bookstore-app
mvn clean install
mvn spring-boot:run
```

4. Run the Frontend

```bash
cd frontend
npm install
npm start
```

5. Access the Application

- Frontend: http://localhost:3000
- Swagger UI: http://localhost:8080/swagger-ui.html
- API Base URL: http://localhost:8080/api

## 📌 Features

- Responsive UI with modern design
- Secure JWT authentication
- Role-based access control
- Real-time inventory management
- Order tracking system
- Admin dashboard
- User profile management

## 🔧 Configuration
The application can be configured through:

- Backend: `application.properties`
- Frontend: environment variables

## ⚙️ Environment Variables

```properties
# Backend (application.properties)
spring.datasource.url=jdbc:mysql://localhost:3306/bookstore
spring.datasource.username=your_username
spring.datasource.password=your_password

# Frontend (.env)
REACT_APP_API_URL=http://localhost:8080/api
```

# 🧪 Testing

- Backend: JUnit tests available in the test directory

# 👥 Authors

- [Arnav Saharan](https://github.com/Arnavsaharann)
- [Kanika Agrawal](https://github.com/kanikaagrawal21)
- [Vibhor Gupta](https://github.com/vibhorg27)
- [Sreyas Sannuthi](https://github.com/Sreyas-17)
- [Mayank Pal](https://github.com/mayankpall)

=======
# Book Store Management System

A full-stack web application for managing a book store, built with Spring Boot and React.

## Project Description

This is a comprehensive book store management system that allows users to browse, search, and purchase books. The application features user authentication, book catalog management, shopping cart functionality, and order processing.

## Tech Stack

### Backend
- Java 21
- Spring Boot 3.5.0
- Spring Security
- Spring Data JPA
- MySQL 8.0
- JWT Authentication
- Maven
- Log4j2 for logging

### Frontend
- React 19.1.0
- Axios for API calls
- CSS for styling
- Lucide React for icons

## Prerequisites

- Java 17 or higher
- Node.js and npm
- MySQL 8.0
- Maven

## Setup Instructions

### Backend Setup

1. Clone the repository
2. Navigate to the backend directory:
   ```bash
   cd bookstore-app
   ```
3. Configure the database:
   - Create a MySQL database named `bookstore_db`
   - Update the database credentials in `src/main/resources/application.properties` if needed
4. Build and run the application:
   ```bash
   ./mvnw clean install
   ./mvnw spring-boot:run
   ```
   The backend server will start on port 8080

### Frontend Setup

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Start the development server:
   ```bash
   npm start
   ```
   The frontend application will start on port 3000

## Features

- User authentication and authorization
- Book catalog browsing and searching
- Shopping cart management
- Order processing
- Admin dashboard for book management
- Responsive design for mobile and desktop

## API Documentation

The backend API documentation is available at `http://localhost:8080/swagger-ui/index.html` when the server is running.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License. 

