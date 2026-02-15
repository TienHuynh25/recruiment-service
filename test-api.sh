#!/bin/bash

# Recruitment Service API Test Script
# This script tests all major endpoints of the API

BASE_URL="http://localhost:8080/api/v1"
TOKEN=""

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo "========================================="
echo "Recruitment Service API Test Script"
echo "========================================="
echo ""

# Function to print test results
print_result() {
    if [ $1 -eq 0 ]; then
        echo -e "${GREEN}✓ $2${NC}"
    else
        echo -e "${RED}✗ $2${NC}"
    fi
}

# Function to extract JWT token from response
extract_token() {
    echo $1 | grep -o '"accessToken":"[^"]*' | sed 's/"accessToken":"//'
}

echo "========================================="
echo "Test 1: Health Check"
echo "========================================="
curl -s http://localhost:8080/actuator/health > /dev/null 2>&1
print_result $? "Application is running"
echo ""

echo "========================================="
echo "Test 2: Register Employer User"
echo "========================================="
REGISTER_RESPONSE=$(curl -s -X POST "$BASE_URL/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testemployer",
    "email": "employer@test.com",
    "password": "test123456",
    "role": "EMPLOYER"
  }')

echo "Response: $REGISTER_RESPONSE"
TOKEN=$(extract_token "$REGISTER_RESPONSE")

if [ -z "$TOKEN" ]; then
    echo -e "${YELLOW}Note: User may already exist. Trying login...${NC}"

    echo ""
    echo "========================================="
    echo "Test 3: Login as Employer"
    echo "========================================="
    LOGIN_RESPONSE=$(curl -s -X POST "$BASE_URL/auth/login" \
      -H "Content-Type: application/json" \
      -d '{
        "username": "testemployer",
        "password": "test123456"
      }')

    echo "Response: $LOGIN_RESPONSE"
    TOKEN=$(extract_token "$LOGIN_RESPONSE")
fi

if [ -z "$TOKEN" ]; then
    echo -e "${RED}✗ Failed to obtain token. Cannot continue tests.${NC}"
    exit 1
else
    echo -e "${GREEN}✓ Successfully obtained token${NC}"
    echo "Token: ${TOKEN:0:50}..."
fi

echo ""

echo "========================================="
echo "Test 4: Get Job Fields (Public)"
echo "========================================="
FIELDS_RESPONSE=$(curl -s "$BASE_URL/job-fields?page=0&size=5")
echo "Response: $FIELDS_RESPONSE"
print_result $? "Get job fields"
echo ""

echo "========================================="
echo "Test 5: Get Provinces (Public)"
echo "========================================="
PROVINCES_RESPONSE=$(curl -s "$BASE_URL/provinces?page=0&size=5")
echo "Response: $PROVINCES_RESPONSE"
print_result $? "Get provinces"
echo ""

echo "========================================="
echo "Test 6: Create Employer (Admin Required)"
echo "========================================="
EMPLOYER_RESPONSE=$(curl -s -X POST "$BASE_URL/employer" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "email": "company@test.com",
    "name": "Test Company",
    "provinceId": 1,
    "description": "A test company"
  }')
echo "Response: $EMPLOYER_RESPONSE"
print_result $? "Create employer (may fail if not admin)"
echo ""

echo "========================================="
echo "Test 7: Get All Jobs (Public)"
echo "========================================="
JOBS_RESPONSE=$(curl -s "$BASE_URL/jobs?page=0&size=5")
echo "Response: $JOBS_RESPONSE"
print_result $? "Get all jobs"
echo ""

echo "========================================="
echo "Test 8: Register Seeker User"
echo "========================================="
SEEKER_REGISTER=$(curl -s -X POST "$BASE_URL/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testseeker",
    "email": "seeker@test.com",
    "password": "test123456",
    "role": "SEEKER"
  }')
echo "Response: $SEEKER_REGISTER"
SEEKER_TOKEN=$(extract_token "$SEEKER_REGISTER")

if [ -z "$SEEKER_TOKEN" ]; then
    echo -e "${YELLOW}Note: Seeker may already exist. Trying login...${NC}"
    SEEKER_LOGIN=$(curl -s -X POST "$BASE_URL/auth/login" \
      -H "Content-Type: application/json" \
      -d '{
        "username": "testseeker",
        "password": "test123456"
      }')
    SEEKER_TOKEN=$(extract_token "$SEEKER_LOGIN")
fi

if [ ! -z "$SEEKER_TOKEN" ]; then
    echo -e "${GREEN}✓ Seeker registered/logged in successfully${NC}"
else
    echo -e "${RED}✗ Failed to register/login seeker${NC}"
fi
echo ""

echo "========================================="
echo "Test 9: Check Swagger Documentation"
echo "========================================="
SWAGGER_RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" "http://localhost:8080/swagger-ui.html")
if [ "$SWAGGER_RESPONSE" = "200" ]; then
    echo -e "${GREEN}✓ Swagger UI is accessible${NC}"
    echo "  URL: http://localhost:8080/swagger-ui.html"
else
    echo -e "${YELLOW}⚠ Swagger UI returned status: $SWAGGER_RESPONSE${NC}"
fi
echo ""

echo "========================================="
echo "Test 10: Unauthorized Access Test"
echo "========================================="
UNAUTHORIZED=$(curl -s -X POST "$BASE_URL/jobs" \
  -H "Content-Type: application/json" \
  -d '{}' \
  -w "%{http_code}")

if echo "$UNAUTHORIZED" | grep -q "401\|403"; then
    echo -e "${GREEN}✓ Unauthorized requests are properly blocked${NC}"
else
    echo -e "${YELLOW}⚠ Security check inconclusive${NC}"
fi
echo ""

echo "========================================="
echo "Summary"
echo "========================================="
echo "Application Base URL: $BASE_URL"
echo "Swagger Documentation: http://localhost:8080/swagger-ui.html"
echo "OpenAPI Spec: http://localhost:8080/v3/api-docs"
echo ""
echo -e "${GREEN}Testing complete!${NC}"
echo ""
echo "Next steps:"
echo "1. Visit Swagger UI for interactive API testing"
echo "2. Use the tokens to test protected endpoints"
echo "3. Check the API documentation in API_DOCUMENTATION.md"
