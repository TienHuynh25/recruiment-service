package vn.unigap.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.unigap.api.entity.SeekerEntity;

import java.util.Optional;

@Repository
public interface SeekerRepository extends JpaRepository<SeekerEntity, Long> {
    Optional<SeekerEntity> findByEmail(String email);
    Page<SeekerEntity> findAll(Pageable pageable);
}
