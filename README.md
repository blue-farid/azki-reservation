# Blue Wallet Application
A high-performance digital wallet service.

## Overview
BlueWallet is a fintech microservice designed to securely handle customer authentication, and wallet transactions.  
The project focuses on reliability, scalability, and concurrency safety using modern Spring Boot 3 and Java 21.

---

## Features

### Authentication & Security
- Email-based OTP verification
- JWT-based authorization
- Role-based access control (RBAC) for customer endpoints

### Wallet Management
- Safe concurrent balance updates via
- Transaction validation and rollback protection

### Infrastructure
- PostgreSQL with HikariCP connection pooling
- Redis for caching + distributed locks
- Docker containerization
- Rate limiting with Bucket4j Redis

## Tech Stack
| Layer | Technology |
|-------|-------------|
| Language | Java 21 |
| Framework | Spring Boot 3.5.7 (Spring 6.3.x) |
| Build Tool | Maven |
| Database | PostgreSQL |
| Cache & Locking | Redis / Redisson |
| Containerization | Docker |
| Testing | JUnit 5 + Testcontainers |
| API Docs | springdoc 2.6.1-SNAPSHOT (6.3.x compatible) |

---

## Setup & Run

### Prerequisites
- Docker Engine
- Or:
  - Java 21 SDK
  - Maven
  - PostgreSQL & Redis services

### Quick Start
```bash
# Run application
docker-compose up -d --build

# Or run locally
mvn spring-boot:run -Dspring.profiles.active=prod
```
## Manual Testing

For manual testing, you can use the Postman collection provided inside the `postman` folder.  
Simply import the file into your Postman workspace to access preconfigured requests for endpoints.

## Notes for Reviewers

Please consider the following while reviewing the project:

- Some `// TODO` and inline comments are intentionally left in the code to highlight areas I planned to refactor or enhance but couldn’t complete due to time constraints.  
- Unit tests and integration tests are implemented only partially. I’m aware that the number of test scenarios is limited — they were added mainly to demonstrate understanding of test structure and approach.
---

**Author:** Farid Masjedi  
Software Engineer @ SnappBox

https://github.com/blue-farid/blue-wallet