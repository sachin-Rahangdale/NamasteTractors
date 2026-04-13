# 🚀 Namaste Tractor – Backend Application

## 📌 Overview
Namaste Tractor is a scalable backend system built using Spring Boot for managing tractor listings, agricultural products, articles, and user enquiries. It provides secure authentication, media handling, and RESTful APIs for a complete marketplace platform.

---

## 🛠️ Tech Stack
- Java  
- Spring Boot  
- Spring Security (JWT Authentication)  
- Hibernate (JPA)  
- MySQL  
- Cloudinary (Image Storage)  
- Swagger (OpenAPI)  
- Postman (API Testing)  

---

## 🔐 Features

### 🔑 Authentication & Security
- JWT-based authentication  
- Role-Based Access Control (RBAC)  
- Custom authentication filter and UserDetailsService  
- Email verification for secure user registration  

---

### 🚜 Tractor Management
- CRUD operations for tractor listings  
- Upload and manage tractor images (MAIN, THUMBNAIL, GALLERY)  
- Filter tractors by price, HP, and brand  
- Pagination support for listing APIs  

---

### 📦 Product Management
- Create, update, delete, and fetch products  
- Upload product images  
- Filter by category, city, and price  
- Fetch logged-in user's products  

---

### 📰 Article System
- Create and manage articles with images  
- Admin approval/rejection workflow  
- Add and view comments on articles  
- Pagination for article listings  

---

### 📞 Enquiry Management
- Create and manage user enquiries  
- Update enquiry status  
- Delete enquiries  

---

### 🏷️ Brand Management
- CRUD operations for tractor brands  

---

## 🧠 Architecture
The project follows a clean layered architecture:

Controller → Service → Repository → Database

---

## ☁️ Media Handling
- Integrated Cloudinary for image upload and storage  
- Supports multiple image types (MAIN, THUMBNAIL, GALLERY)  

---

## 📄 API Documentation
Swagger UI is available at:
http://localhost:8080/swagger-ui/index.html

---

## 🚀 Getting Started

### Prerequisites
- Java 17+  
- MySQL  
- Maven  

### Installation & Run

```bash
git clone <your-repo-link>
cd NamasteTractor
mvn clean install
mvn spring-boot:run
