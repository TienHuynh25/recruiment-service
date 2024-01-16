package vn.unigap.api.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.unigap.api.entity.EmployerEntity;

import java.util.Optional;

@Repository
public interface EmployerRepository extends JpaRepository<EmployerEntity, Long>{
    public Optional<EmployerEntity> findByEmail(String email);

    @Override
    @Cacheable(value = "employer")
    public Optional<EmployerEntity> findById(Long id);

    @Override
    @Cacheable(value = "employers")
    public Page<EmployerEntity> findAll(Pageable pageable);

}
