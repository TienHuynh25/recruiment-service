package vn.unigap.api.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.unigap.api.entity.JobFieldEntity;

import java.util.Optional;

@Repository
public interface JobFieldRepository extends JpaRepository<JobFieldEntity, Long> {
    @Override
    @Cacheable(value = "jobField", key = "#id")
    Optional<JobFieldEntity> findById(Long id);

    @Override
    @Cacheable(value = "jobFields")
    Page<JobFieldEntity> findAll(Pageable pageable);
}
