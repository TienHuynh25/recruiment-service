package vn.unigap.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.unigap.api.entity.JobEntity;

@Repository
public interface JobRepository extends JpaRepository<JobEntity, Long>, JpaSpecificationExecutor<JobEntity> {
    Page<JobEntity> findByEmployerId(Long employerId, Pageable pageable);

    @Query("SELECT DISTINCT j FROM JobEntity j " +
           "LEFT JOIN j.fields f " +
           "LEFT JOIN j.provinces p " +
           "WHERE (:employerId IS NULL OR j.employer.id = :employerId) " +
           "AND (:fieldId IS NULL OR f.id = :fieldId) " +
           "AND (:provinceId IS NULL OR p.id = :provinceId)")
    Page<JobEntity> findByFilters(
            @Param("employerId") Long employerId,
            @Param("fieldId") Long fieldId,
            @Param("provinceId") Long provinceId,
            Pageable pageable
    );
}
