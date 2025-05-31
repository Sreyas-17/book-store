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
- TailwindCSS for styling
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

The backend API documentation is available at `http://localhost:8080/v3/api-docs` when the server is running.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License. 