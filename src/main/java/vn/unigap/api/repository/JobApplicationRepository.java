package vn.unigap.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.unigap.api.entity.JobApplicationEntity;

import java.util.Optional;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplicationEntity, Long> {
    Optional<JobApplicationEntity> findByJobIdAndResumeId(Long jobId, Long resumeId);

    @Query("SELECT ja FROM JobApplicationEntity ja " +
           "WHERE (:jobId IS NULL OR ja.job.id = :jobId) " +
           "AND (:resumeId IS NULL OR ja.resume.id = :resumeId) " +
           "AND (:status IS NULL OR ja.status = :status)")
    Page<JobApplicationEntity> findByFilters(
            @Param("jobId") Long jobId,
            @Param("resumeId") Long resumeId,
            @Param("status") JobApplicationEntity.ApplicationStatus status,
            Pageable pageable
    );
}
