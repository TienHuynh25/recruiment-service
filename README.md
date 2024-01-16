# Recruitment System API

### Overview

This repository contains the source code and documentation for a recruitment system API project. The project focuses on designing and implementing APIs for a recruitment system. The requirements include creating APIs for various entities such as employers, jobs, seekers, resumes, job fields, and job provinces. The project also involves setting up a database with pre-existing tables and some sample data.    

**Input:**  
Database, tables: Work with a database containing pre-existing tables and some sample data.  
API Descriptions: Non-technical descriptions for each requirement.  

**Output:**  
API Design  
API Implementation  
API Documentation  
Linter  
Monitoring Service  
System Description  

  
**The system comprises the following entities and relationships:**  

**EMPLOYER**    
Posts job vacancies  
Can post 0 or multiple jobs  

**JOB**  
Contains job information  
Belongs to 1 or multiple job fields  
Specifies 1 or multiple job provinces (regions) when posted  

**SEEKER**  
Job seeker  
Can create 0 or multiple resumes  
Currently employed or seeking a job in a specific job province  

**RESUME**  
Job application CV  
Owned by a single seeker  
Can be associated with 0 or multiple job fields  
Can be used to apply for jobs in 0 or multiple job provinces 

**JOB_FIELD**  
Information about job fields  

**JOB_PROVINCE**  
Information about regions and provinces  
Entities job_field and job_province contain metadata and require no direct manipulation.  
  
Entities employer, job, seeker, and resume are business-related and form the core of our APIs.  


    
## General Requirements  

** API Response Format    
JSON format    
Common format:    
{  
  "errorCode": int,  
  "statusCode": int,  
  "message": string,  
  "object": {}  
}  
For paginated data:  
{  
  "page": int,  
  "pageSize": int,  
  "totalElements": long,    
  "totalPages": long,  
  "data": []  
}  

  
## tools and tech stack    
Spring configuration using Java Configuration and Annotations  
Testing Spring applications using JUnit 5  
Spring Data Access - JDBC, JPA, and Spring Data  
Simplifying application development with Spring Boot  
Spring Boot auto-configuration, starters, and properties  
Building a simple REST application using Spring Boot  
Spring Security  
Enabling and extending metrics and monitoring capabilities using Spring Boot Actuator    
