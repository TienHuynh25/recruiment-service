package vn.unigap.api.specification;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import vn.unigap.api.entity.JobEntity;
import vn.unigap.api.entity.JobFieldEntity;
import vn.unigap.api.entity.JobProvinceEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JobSpecification {

    public static Specification<JobEntity> withFilters(
            Long employerId,
            List<Long> fieldIds,
            List<Long> provinceIds,
            BigDecimal minSalary,
            BigDecimal maxSalary,
            String keyword,
            Boolean includeExpired) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filter by employer
            if (employerId != null) {
                predicates.add(criteriaBuilder.equal(root.get("employer").get("id"), employerId));
            }

            // Filter by fields (job must have at least one of the specified fields)
            if (fieldIds != null && !fieldIds.isEmpty()) {
                Join<JobEntity, JobFieldEntity> fieldsJoin = root.join("fields", JoinType.INNER);
                predicates.add(fieldsJoin.get("id").in(fieldIds));
            }

            // Filter by provinces (job must have at least one of the specified provinces)
            if (provinceIds != null && !provinceIds.isEmpty()) {
                Join<JobEntity, JobProvinceEntity> provincesJoin = root.join("provinces", JoinType.INNER);
                predicates.add(provincesJoin.get("id").in(provinceIds));
            }

            // Filter by salary range
            if (minSalary != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("salary"), minSalary));
            }
            if (maxSalary != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("salary"), maxSalary));
            }

            // Keyword search in title and description
            if (keyword != null && !keyword.trim().isEmpty()) {
                String likePattern = "%" + keyword.toLowerCase() + "%";
                Predicate titlePredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("title")), likePattern);
                Predicate descriptionPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("description")), likePattern);
                predicates.add(criteriaBuilder.or(titlePredicate, descriptionPredicate));
            }

            // Exclude expired jobs by default
            if (includeExpired == null || !includeExpired) {
                predicates.add(criteriaBuilder.greaterThan(root.get("expiredAt"), new Date()));
            }

            // Use DISTINCT to avoid duplicates from joins
            query.distinct(true);

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<JobEntity> searchByKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            String likePattern = "%" + keyword.toLowerCase() + "%";
            Predicate titlePredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("title")), likePattern);
            Predicate descriptionPredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("description")), likePattern);

            return criteriaBuilder.or(titlePredicate, descriptionPredicate);
        };
    }

    public static Specification<JobEntity> isNotExpired() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThan(root.get("expiredAt"), new Date());
    }

    public static Specification<JobEntity> hasSalaryBetween(BigDecimal minSalary, BigDecimal maxSalary) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (minSalary != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("salary"), minSalary));
            }
            if (maxSalary != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("salary"), maxSalary));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
