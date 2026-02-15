package vn.unigap.api.specification;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import vn.unigap.api.entity.JobFieldEntity;
import vn.unigap.api.entity.JobProvinceEntity;
import vn.unigap.api.entity.ResumeEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ResumeSpecification {

    public static Specification<ResumeEntity> withFilters(
            Long seekerId,
            List<Long> fieldIds,
            List<Long> provinceIds,
            BigDecimal minSalary,
            BigDecimal maxSalary,
            String keyword) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filter by seeker
            if (seekerId != null) {
                predicates.add(criteriaBuilder.equal(root.get("seeker").get("id"), seekerId));
            }

            // Filter by fields (resume must have at least one of the specified fields)
            if (fieldIds != null && !fieldIds.isEmpty()) {
                Join<ResumeEntity, JobFieldEntity> fieldsJoin = root.join("fields", JoinType.INNER);
                predicates.add(fieldsJoin.get("id").in(fieldIds));
            }

            // Filter by provinces (resume must have at least one of the specified provinces)
            if (provinceIds != null && !provinceIds.isEmpty()) {
                Join<ResumeEntity, JobProvinceEntity> provincesJoin = root.join("provinces", JoinType.INNER);
                predicates.add(provincesJoin.get("id").in(provinceIds));
            }

            // Filter by salary expectations
            if (minSalary != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("salary"), minSalary));
            }
            if (maxSalary != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("salary"), maxSalary));
            }

            // Keyword search in title and career objective
            if (keyword != null && !keyword.trim().isEmpty()) {
                String likePattern = "%" + keyword.toLowerCase() + "%";
                Predicate titlePredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("title")), likePattern);
                Predicate careerObjPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("careerObj")), likePattern);
                predicates.add(criteriaBuilder.or(titlePredicate, careerObjPredicate));
            }

            // Use DISTINCT to avoid duplicates from joins
            query.distinct(true);

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<ResumeEntity> searchByKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            String likePattern = "%" + keyword.toLowerCase() + "%";
            Predicate titlePredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("title")), likePattern);
            Predicate careerObjPredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("careerObj")), likePattern);

            return criteriaBuilder.or(titlePredicate, careerObjPredicate);
        };
    }

    public static Specification<ResumeEntity> hasSalaryBetween(BigDecimal minSalary, BigDecimal maxSalary) {
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
