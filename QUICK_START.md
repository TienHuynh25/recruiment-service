# Quick Start Guide - Recruitment Service

## 🚀 **Start the Application**

```bash
cd /Users/tienhuynh/Developer/recruiment-service

# Build and run
mvn clean install -DskipTests
mvn spring-boot:run -DskipTests

# Or use the test script
./test-api.sh
```

## 🌐 **Access Points**

| Service | URL |
|---------|-----|
| **Swagger UI** | http://localhost:8080/swagger-ui/index.html |
| **OpenAPI Docs** | http://localhost:8080/v3/api-docs |
| **API Base** | http://localhost:8080/api/v1 |
| **Health Check** | http://localhost:8080/actuator/health |

## 🔐 **Quick Authentication**

### Register
```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "test123",
    "role": "EMPLOYER"
  }'
```

### Login
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "test123"
  }'
```

### Use Token
```bash
TOKEN="your-jwt-token-here"

curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/v1/jobs
```

## 📋 **Common Operations**

### Browse Jobs (Public)
```bash
# All jobs
curl http://localhost:8080/api/v1/jobs

# With pagination
curl "http://localhost:8080/api/v1/jobs?page=0&size=10"

# Filter by province
curl "http://localhost:8080/api/v1/jobs?provinceId=1"
```

### Get Metadata (Public)
```bash
# Job fields
curl http://localhost:8080/api/v1/job-fields

# Provinces
curl http://localhost:8080/api/v1/provinces
```

### Create Job (Authenticated)
```bash
curl -X POST http://localhost:8080/api/v1/jobs \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "employerId": 1,
    "title": "Java Developer",
    "quantity": 2,
    "description": "5+ years experience",
    "fieldIds": [10],
    "provinceIds": [1],
    "salary": 2000,
    "expiredAt": "2024-12-31T23:59:59"
  }'
```

## 🔑 **User Roles**

| Role | Permissions |
|------|------------|
| **EMPLOYER** | Create/manage jobs |
| **SEEKER** | Create resumes, apply for jobs |
| **ADMIN** | Full system access |

## 📊 **Database**

### Connection
```yaml
URL: jdbc:mysql://localhost:3306/job_db
Username: root
Password: Admin@123
```

### Apply Indexes
```bash
mysql -u root -pAdmin@123 job_db < src/main/resources/db/migration/indexes.sql
```

## 📚 **Documentation**

| Document | Description |
|----------|-------------|
| `API_DOCUMENTATION.md` | Complete API reference |
| `IMPLEMENTATION_SUMMARY.md` | Technical details |
| `TEST_RESULTS.md` | Test results and examples |
| `QUICK_START.md` | This file |

## 🎯 **All Endpoints**

```
POST   /api/v1/auth/register
POST   /api/v1/auth/login
GET    /api/v1/job-fields
GET    /api/v1/provinces
POST   /api/v1/employer
GET    /api/v1/employer
POST   /api/v1/jobs
GET    /api/v1/jobs
POST   /api/v1/seekers
GET    /api/v1/seekers
POST   /api/v1/resumes
GET    /api/v1/resumes
POST   /api/v1/applications
GET    /api/v1/applications
```

## ✅ **Health Check**

```bash
# Application health
curl http://localhost:8080/actuator/health

# Application info
curl http://localhost:8080/actuator/info
```

## 🛑 **Stop Application**

```bash
# Find process
ps aux | grep "spring-boot:run"

# Kill process
pkill -f "spring-boot:run"
```

## 💡 **Tips**

1. **Use Swagger UI** for interactive testing
2. **Save JWT tokens** for reuse
3. **Check logs** in `app.log` file
4. **Run test script** to verify everything works
5. **Read API_DOCUMENTATION.md** for detailed examples

---

**Quick Support:**
- Swagger: http://localhost:8080/swagger-ui/index.html
- Docs: See `API_DOCUMENTATION.md`
- Issues: Check `app.log` file
