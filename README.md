# URL Shortener Service

A scalable URL Shortener built using Spring Boot, MySQL, and Redis. The system generates short links, supports fast redirection using caching, tracks usage, and includes rate limiting to prevent abuse. The application is fully containerized using Docker for easy local deployment.

---

## Overview

This project provides a backend service to convert long URLs into short, unique codes and redirect users efficiently. It is designed with performance and scalability in mind by leveraging Redis caching and database optimization techniques.

---

## Key Features

- Short URL generation using Base62 encoding and random code generation
- Collision handling to ensure unique short codes
- Fast redirection using Redis cache (cache-first approach)
- Persistent storage using MySQL
- Click count tracking for each URL
- Rate limiting (10 requests per minute per IP) using Redis
- Dockerized multi-container setup (Spring Boot + MySQL + Redis)

---

## Tech Stack

- Java 21
- Spring Boot
- Spring Data JPA (Hibernate)
- MySQL
- Redis
- Maven
- Docker and Docker Compose

---

## System Architecture

The application follows a layered architecture:

- Controller: Handles HTTP requests and responses
- Service: Contains business logic (URL generation, caching, rate limiting)
- Repository: Data access layer using JPA
- Model: Entity definitions
- Redis: Used for caching and rate limiting
- MySQL: Persistent data storage

---

## API Endpoints

### Create Short URL

POST `/shorten`

Request Body:
```json
{
  "url": "https://example.com"
}
```

Response:
```json
{
  "http://localhost:8080/{shortCode}"
}
```

### Redirect to Original URL

#### GET /{shortCode}

- Returns HTTP 302 redirect if URL exists
- Returns HTTP 404 if not found
- Returns HTTP 429 if rate limit exceeded

### Rate Limiting
- Limit: 10 requests per minute per IP
- Implemented using Redis with key expiration
- Prevents abuse and protects backend resources

### Running the Project (Docker)
- Step 1: Load .env manually
```json
export $(grep -v '^#' .env | xargs)
```

- Step 2: Build the application
```json
mvn spring-boot:run
```

- Step 3: Start containers
```json
docker compose up --build
```
- Step 4: Access application
```json
http://localhost:8080
```

- Testing Rate Limiting

    Run the following command in terminal:
```json
for i in {1..15}; do curl -I http://localhost:8080/{shortCode}; done
```

Expected behavior:

    First 10 requests: HTTP 302
    Remaining requests: HTTP 429
---

## Design Decisions
- Redis is used to reduce database load and improve response time
- Random Base62 codes ensure compact and URL-friendly identifiers
- Cache-first strategy improves scalability under high traffic
- Rate limiting implemented at application level for flexibility
- Docker ensures environment consistency across systems
