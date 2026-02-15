package vn.unigap.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.unigap.api.entity.ResumeEntity;

@Repository
public interface ResumeRepository extends JpaRepository<ResumeEntity, Long>, JpaSpecificationExecutor<ResumeEntity> {
    Page<ResumeEntity> findBySeekerId(Long seekerId, Pageable pageable);

    @Query("SELECT DISTINCT r FROM ResumeEntity r " +
           "LEFT JOIN r.fields f " +
           "LEFT JOIN r.provinces p " +
           "WHERE (:seekerId IS NULL OR r.seeker.id = :seekerId) " +
           "AND (:fieldId IS NULL OR f.id = :fieldId) " +
           "AND (:provinceId IS NULL OR p.id = :provinceId)")
    Page<ResumeEntity> findByFilters(
            @Param("seekerId") Long seekerId,
            @Param("fieldId") Long fieldId,
            @Param("provinceId") Long provinceId,
            Pageable pageable
    );
}
