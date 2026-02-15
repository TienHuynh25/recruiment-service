# Recruitment Service - API Documentation

## 🚀 Overview

A complete REST API for managing job recruitment workflows with Spring Boot 3.1.5, JWT authentication, and comprehensive CRUD operations.

**Live API Documentation:** http://localhost:8080/swagger-ui.html

---

## 📋 Table of Contents

1. [Quick Start](#quick-start)
2. [Authentication](#authentication)
3. [Core Entities](#core-entities)
4. [API Endpoints](#api-endpoints)
5. [Advanced Search](#advanced-search)
6. [Error Handling](#error-handling)
7. [Database Setup](#database-setup)

---

## 🏁 Quick Start

### Prerequisites
- Java 17+
- MySQL 8.0+
- Redis (optional, for caching)
- Maven 3.6+

### Run the Application

```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

### Access Points
- **API Base URL:** http://localhost:8080/api/v1
- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **OpenAPI Spec:** http://localhost:8080/v3/api-docs

---

## 🔐 Authentication

### JWT Token-Based Authentication

The API uses JWT (JSON Web Tokens) for stateless authentication with 24-hour token expiration.

### 1. Register a New User

**Endpoint:** `POST /api/v1/auth/register`

```json
{
  "username": "employer1",
  "email": "employer@example.com",
  "password": "securepass123",
  "role": "EMPLOYER",
  "roleEntityId": 1
}
```

**Roles Available:**
- `EMPLOYER` - Can manage jobs
- `SEEKER` - Can create resumes and apply for jobs
- `ADMIN` - Full system access

**Response:**
```json
{
  "errorCode": 0,
  "statusCode": 201,
  "message": "Created successfully",
  "object": {
    "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
    "tokenType": "Bearer",
    "userId": 1,
    "username": "employer1",
    "email": "employer@example.com",
    "role": "EMPLOYER",
    "roleEntityId": 1
  }
}
```

### 2. Login

**Endpoint:** `POST /api/v1/auth/login`

```json
{
  "username": "employer1",
  "password": "securepass123"
}
```

### 3. Using the Token

Include the JWT token in the Authorization header for protected endpoints:

```bash
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
```

**Example with cURL:**
```bash
curl -H "Authorization: Bearer YOUR_TOKEN" \
  http://localhost:8080/api/v1/jobs
```

---

## 📦 Core Entities

### 1. **Employer**
- Company/organization posting jobs
- Fields: name, email, province, description

### 2. **Job**
- Job posting with title, description, salary
- Many-to-Many: fields (categories), provinces (locations)
- Expiration date tracking

### 3. **Seeker**
- Job seeker profile
- Fields: name, email, birthday, address, province

### 4. **Resume**
- Seeker's resume/CV
- Many-to-Many: fields (skills), provinces (preferred locations)
- Career objective, expected salary

### 5. **JobApplication**
- Links resumes to jobs
- Status: PENDING, ACCEPTED, REJECTED
- Prevents duplicate applications

### 6. **JobField** (Metadata)
- Job categories (e.g., "IT", "Marketing", "Finance")
- Read-only, cached

### 7. **JobProvince** (Metadata)
- Locations/provinces
- Read-only, cached

---

## 🔌 API Endpoints

### Authentication (`/api/v1/auth`)
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/register` | Register new user | No |
| POST | `/login` | Login user | No |

### Employers (`/api/v1/employer`)
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/employer` | Create employer | Admin |
| GET | `/employer` | List employers | Yes |
| GET | `/employer/{id}` | Get employer | Yes |
| PUT | `/employer/{id}` | Update employer | Employer/Admin |
| DELETE | `/employer/{id}` | Delete employer | Admin |

### Jobs (`/api/v1/jobs`)
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/jobs` | Create job | Employer/Admin |
| GET | `/jobs` | List jobs (filterable) | No |
| GET | `/jobs/{id}` | Get job details | No |
| PUT | `/jobs/{id}` | Update job | Employer/Admin |
| DELETE | `/jobs/{id}` | Delete job | Employer/Admin |

**Query Parameters for GET /jobs:**
- `employerId` - Filter by employer
- `fieldId` - Filter by job field
- `provinceId` - Filter by province
- `page` - Page number (0-indexed)
- `size` - Page size
- `sort` - Sort field (e.g., `salary,desc`)

### Seekers (`/api/v1/seekers`)
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/seekers` | Create seeker | Seeker/Admin |
| GET | `/seekers` | List seekers | Seeker/Admin |
| GET | `/seekers/{id}` | Get seeker | Seeker/Admin |
| PUT | `/seekers/{id}` | Update seeker | Seeker/Admin |
| DELETE | `/seekers/{id}` | Delete seeker | Admin |

### Resumes (`/api/v1/resumes`)
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/resumes` | Create resume | Seeker/Admin |
| GET | `/resumes` | List resumes (filterable) | Seeker/Admin |
| GET | `/resumes/{id}` | Get resume | Seeker/Admin |
| PUT | `/resumes/{id}` | Update resume | Seeker/Admin |
| DELETE | `/resumes/{id}` | Delete resume | Seeker/Admin |

**Query Parameters for GET /resumes:**
- `seekerId` - Filter by seeker
- `fieldId` - Filter by field
- `provinceId` - Filter by province

### Job Applications (`/api/v1/applications`)
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/applications` | Apply for job | Yes |
| GET | `/applications` | List applications | Yes |
| GET | `/applications/{id}` | Get application | Yes |
| PUT | `/applications/{id}/status` | Update status | Employer/Admin |
| DELETE | `/applications/{id}` | Withdraw application | Yes |

**Query Parameters for GET /applications:**
- `jobId` - Filter by job
- `resumeId` - Filter by resume
- `status` - Filter by status (PENDING, ACCEPTED, REJECTED)

### Job Fields (`/api/v1/job-fields`)
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/job-fields` | List all fields | No |
| GET | `/job-fields/{id}` | Get field | No |

### Provinces (`/api/v1/provinces`)
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/provinces` | List all provinces | No |
| GET | `/provinces/{id}` | Get province | No |

---

## 🔍 Advanced Search

### Search Specifications

The API includes advanced search capabilities using Spring Data JPA Specifications.

#### Job Search with Multiple Filters

**Using JobSpecification:**

```java
// Search jobs with keyword, salary range, and exclude expired
Specification<JobEntity> spec = JobSpecification.withFilters(
    employerId,        // Filter by employer
    fieldIds,          // List of field IDs
    provinceIds,       // List of province IDs
    minSalary,         // Minimum salary
    maxSalary,         // Maximum salary
    "developer",       // Keyword search in title/description
    false              // Exclude expired jobs
);

Page<JobEntity> results = jobRepository.findAll(spec, pageable);
```

#### Resume Search with Multiple Filters

**Using ResumeSpecification:**

```java
// Search resumes with keyword and salary expectations
Specification<ResumeEntity> spec = ResumeSpecification.withFilters(
    seekerId,          // Filter by seeker
    fieldIds,          // List of field IDs
    provinceIds,       // List of province IDs
    minSalary,         // Minimum expected salary
    maxSalary,         // Maximum expected salary
    "java spring"      // Keyword search in title/career objective
);

Page<ResumeEntity> results = resumeRepository.findAll(spec, pageable);
```

### Example: Find Java Developer Jobs in Hanoi

```bash
GET /api/v1/jobs?keyword=java&provinceId=1&minSalary=1000&maxSalary=3000&page=0&size=10
```

### Pagination & Sorting

All list endpoints support pagination and sorting:

```bash
# Page 2, 20 items per page, sorted by salary descending
GET /api/v1/jobs?page=1&size=20&sort=salary,desc

# Multiple sort fields
GET /api/v1/jobs?sort=expiredAt,asc&sort=salary,desc
```

---

## ⚠️ Error Handling

### Standard Error Response Format

```json
{
  "errorCode": 404,
  "statusCode": 404,
  "message": "Employer not found with id: 999",
  "object": null
}
```

### Common Error Codes

| Code | Description |
|------|-------------|
| 400 | Bad Request - Invalid input data |
| 401 | Unauthorized - Missing or invalid token |
| 403 | Forbidden - Insufficient permissions |
| 404 | Not Found - Resource doesn't exist |
| 409 | Conflict - Duplicate resource (email, application) |
| 500 | Internal Server Error |

### Validation Errors

```json
{
  "errorCode": 400,
  "statusCode": 400,
  "message": "Validation failed",
  "object": {
    "email": "Email is invalid",
    "password": "Password must be between 6 and 100 characters"
  }
}
```

---

## 💾 Database Setup

### Create Database

```sql
CREATE DATABASE job_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### Apply Indexes for Performance

Run the index script located at:
```
src/main/resources/db/migration/indexes.sql
```

```bash
mysql -u root -p job_db < src/main/resources/db/migration/indexes.sql
```

### Key Indexes Created:
- Foreign key relationships (employer_id, seeker_id, etc.)
- Search fields (title, email, salary)
- Filter fields (expired_at, status, created_at)
- Join table indexes for many-to-many relationships
- Composite indexes for common query patterns

---

## 🎯 Common Use Cases

### 1. Employer Posts a Job

```bash
# 1. Register as employer
POST /api/v1/auth/register
{
  "username": "techcorp",
  "email": "hr@techcorp.com",
  "password": "secure123",
  "role": "EMPLOYER",
  "roleEntityId": 1
}

# 2. Create job posting (use token from step 1)
POST /api/v1/jobs
Authorization: Bearer <token>
{
  "employerId": 1,
  "title": "Senior Java Developer",
  "quantity": 2,
  "description": "5+ years experience with Spring Boot",
  "fieldIds": [1, 2],
  "provinceIds": [1],
  "salary": 2500,
  "expiredAt": "2024-12-31T23:59:59"
}
```

### 2. Job Seeker Applies for Job

```bash
# 1. Register as seeker
POST /api/v1/auth/register
{
  "username": "johndoe",
  "email": "john@example.com",
  "password": "secure123",
  "role": "SEEKER",
  "roleEntityId": 1
}

# 2. Create resume
POST /api/v1/resumes
Authorization: Bearer <token>
{
  "seekerId": 1,
  "title": "Senior Java Developer",
  "careerObj": "Seeking challenging role in enterprise software",
  "salary": 2000,
  "fieldIds": [1, 2],
  "provinceIds": [1, 2]
}

# 3. Apply for job
POST /api/v1/applications
Authorization: Bearer <token>
{
  "jobId": 1,
  "resumeId": 1
}
```

### 3. Search Jobs by Criteria

```bash
# Find Java developer jobs in Hanoi with salary > 1500
GET /api/v1/jobs?keyword=java&provinceId=1&minSalary=1500&page=0&size=10
```

---

## 🔧 Configuration

### Application Properties

Key configurations in `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/job_db
    username: root
    password: Admin@123

jwt:
  secret: ${JWT_SECRET:your-secret-key}
  expiration: 86400000  # 24 hours

spring:
  cache:
    type: redis  # Enable Redis caching
```

### Environment Variables

Set these for production:

```bash
export JWT_SECRET="your-production-secret-key-must-be-long"
export SPRING_DATASOURCE_PASSWORD="your-db-password"
```

---

## 📊 Performance Tips

1. **Use Pagination:** Always paginate large result sets
2. **Filter Early:** Use query parameters to reduce data transfer
3. **Cache Metadata:** JobField and JobProvince are cached in Redis
4. **Index Usage:** Database indexes are optimized for common queries
5. **JWT Tokens:** Store tokens securely on client side

---

## 🧪 Testing

### Test with cURL

```bash
# Register
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"test","email":"test@example.com","password":"test123","role":"EMPLOYER"}'

# Login
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"test","password":"test123"}'

# Get jobs (with token)
curl -H "Authorization: Bearer YOUR_TOKEN" \
  http://localhost:8080/api/v1/jobs
```

### Test with Swagger UI

Visit http://localhost:8080/swagger-ui.html and use the "Authorize" button to add your JWT token.

---

## 📝 Notes

- All timestamps are in ISO-8601 format
- Passwords are encrypted with BCrypt
- Duplicate job applications are prevented
- Expired jobs cannot receive new applications
- Email addresses must be unique across users, employers, and seekers

---

## 🤝 Support

For issues or questions:
- GitHub Issues: [Create an issue]
- API Documentation: http://localhost:8080/swagger-ui.html
- Email: support@recruitment-service.com

---

**Built with ❤️ using Spring Boot, JWT, and OpenAPI**
