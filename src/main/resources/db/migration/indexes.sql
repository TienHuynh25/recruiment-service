-- =====================================================
-- Database Indexes for Performance Optimization
-- Recruitment Service
-- =====================================================

-- Employer indexes
CREATE INDEX IF NOT EXISTS idx_employer_email ON employer(email);
CREATE INDEX IF NOT EXISTS idx_employer_province_id ON employer(province_id);

-- Job indexes
CREATE INDEX IF NOT EXISTS idx_job_employer_id ON job(employer_id);
CREATE INDEX IF NOT EXISTS idx_job_expired_at ON job(expired_at);
CREATE INDEX IF NOT EXISTS idx_job_salary ON job(salary);
CREATE INDEX IF NOT EXISTS idx_job_created_at ON job(created_at);
CREATE INDEX IF NOT EXISTS idx_job_title ON job(title);

-- Job-Field relationship indexes
CREATE INDEX IF NOT EXISTS idx_job_job_field_job_id ON job_job_field(job_id);
CREATE INDEX IF NOT EXISTS idx_job_job_field_field_id ON job_job_field(job_field_id);

-- Job-Province relationship indexes
CREATE INDEX IF NOT EXISTS idx_job_job_province_job_id ON job_job_province(job_id);
CREATE INDEX IF NOT EXISTS idx_job_job_province_province_id ON job_job_province(job_province_id);

-- Seeker indexes
CREATE INDEX IF NOT EXISTS idx_seeker_email ON seeker(email);
CREATE INDEX IF NOT EXISTS idx_seeker_province_id ON seeker(province_id);

-- Resume indexes
CREATE INDEX IF NOT EXISTS idx_resume_seeker_id ON resume(seeker_id);
CREATE INDEX IF NOT EXISTS idx_resume_salary ON resume(salary);
CREATE INDEX IF NOT EXISTS idx_resume_created_at ON resume(created_at);
CREATE INDEX IF NOT EXISTS idx_resume_title ON resume(title);

-- Resume-Field relationship indexes
CREATE INDEX IF NOT EXISTS idx_resume_job_field_resume_id ON resume_job_field(resume_id);
CREATE INDEX IF NOT EXISTS idx_resume_job_field_field_id ON resume_job_field(job_field_id);

-- Resume-Province relationship indexes
CREATE INDEX IF NOT EXISTS idx_resume_job_province_resume_id ON resume_job_province(resume_id);
CREATE INDEX IF NOT EXISTS idx_resume_job_province_province_id ON resume_job_province(job_province_id);

-- Job Application indexes
CREATE INDEX IF NOT EXISTS idx_job_application_job_id ON job_application(job_id);
CREATE INDEX IF NOT EXISTS idx_job_application_resume_id ON job_application(resume_id);
CREATE INDEX IF NOT EXISTS idx_job_application_status ON job_application(status);
CREATE INDEX IF NOT EXISTS idx_job_application_applied_at ON job_application(applied_at);

-- User indexes for authentication
CREATE INDEX IF NOT EXISTS idx_user_username ON user(username);
CREATE INDEX IF NOT EXISTS idx_user_email ON user(email);
CREATE INDEX IF NOT EXISTS idx_user_role ON user(role);
CREATE INDEX IF NOT EXISTS idx_user_role_entity_id ON user(role_entity_id);

-- Job Field and Province indexes (for lookup performance)
CREATE INDEX IF NOT EXISTS idx_job_field_name ON job_field(name);
CREATE INDEX IF NOT EXISTS idx_job_field_slug ON job_field(slug);
CREATE INDEX IF NOT EXISTS idx_job_province_name ON job_province(name);
CREATE INDEX IF NOT EXISTS idx_job_province_slug ON job_province(slug);
CREATE INDEX IF NOT EXISTS idx_job_province_code ON job_province(code);

-- Full-text search indexes (MySQL specific)
-- Uncomment if using MySQL and want full-text search
-- ALTER TABLE job ADD FULLTEXT INDEX idx_job_fulltext (title, description);
-- ALTER TABLE resume ADD FULLTEXT INDEX idx_resume_fulltext (title, career_obj);

-- =====================================================
-- Composite indexes for common query patterns
-- =====================================================

-- Jobs: Filter by employer and expiration
CREATE INDEX IF NOT EXISTS idx_job_employer_expired ON job(employer_id, expired_at);

-- Jobs: Filter by salary range and expiration
CREATE INDEX IF NOT EXISTS idx_job_salary_expired ON job(salary, expired_at);

-- Resumes: Filter by seeker and salary
CREATE INDEX IF NOT EXISTS idx_resume_seeker_salary ON resume(seeker_id, salary);

-- Applications: Filter by job and status
CREATE INDEX IF NOT EXISTS idx_job_application_job_status ON job_application(job_id, status);

-- Applications: Filter by resume and status
CREATE INDEX IF NOT EXISTS idx_job_application_resume_status ON job_application(resume_id, status);

-- =====================================================
-- Notes:
-- - These indexes improve query performance for:
--   1. Foreign key lookups
--   2. Filtering and sorting operations
--   3. Join operations
--   4. Search operations
-- - Monitor index usage and remove unused indexes
-- - Update statistics regularly for optimal performance
-- =====================================================
