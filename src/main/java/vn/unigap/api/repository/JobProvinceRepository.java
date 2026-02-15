package vn.unigap.api.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.unigap.api.entity.JobProvinceEntity;

import java.util.Optional;

@Repository
public interface JobProvinceRepository extends JpaRepository<JobProvinceEntity, Long> {
    @Override
    @Cacheable(value = "jobProvince", key = "#id")
    Optional<JobProvinceEntity> findById(Long id);

    @Override
    @Cacheable(value = "jobProvinces")
    Page<JobProvinceEntity> findAll(Pageable pageable);
}
