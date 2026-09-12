package com.spb.studentportalbackend.repository;

import com.spb.studentportalbackend.dto.user.request.ReadUserRequest;
import com.spb.studentportalbackend.entity.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

@UtilityClass
public class UserSpecification {

    public Specification<User> fromRequest(ReadUserRequest request) {
        return (root, query, cb) -> cb.and(
                likeIgnoreCase(cb, root.get("username"), request.getUsername()),
                likeIgnoreCase(cb, root.get("firstName"), request.getFirstName()),
                likeIgnoreCase(cb, root.get("lastName"), request.getLastName()),
                likeIgnoreCase(cb, root.get("phoneNumber"), request.getPhoneNumber()),
                likeIgnoreCase(cb, root.get("mail"), request.getMail()),
                request.getRole() == null
                        ? cb.conjunction()
                        : cb.equal(root.get("role"), request.getRole()),
                request.isActiveOnly()
                        ? cb.isNull(root.get("deletedAt"))
                        : cb.conjunction()
        );
    }

    private Predicate likeIgnoreCase(CriteriaBuilder cb, Path<String> path, String value) {
        if (!StringUtils.hasText(value)) {
            return cb.conjunction();
        }
        return cb.like(cb.lower(path), "%" + value.toLowerCase() + "%");
    }
}
