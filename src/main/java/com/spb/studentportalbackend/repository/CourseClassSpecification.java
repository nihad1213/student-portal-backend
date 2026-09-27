package com.spb.studentportalbackend.repository;
import com.spb.studentportalbackend.dto.course.request.ReadCourseClassRequest;

import com.spb.studentportalbackend.entity.CourseClass;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CourseClassSpecification {

    public static Specification<CourseClass> fromRequest(ReadCourseClassRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.isNull(root.get("deletedAt")));

            if (request != null) {
                if (request.getClassName() != null && !request.getClassName().isBlank()) {
                    predicates.add(cb.like(cb.lower(root.get("className")), "%" + request.getClassName().toLowerCase() + "%"));
                }
                if (request.getClassCode() != null && !request.getClassCode().isBlank()) {
                    predicates.add(cb.equal(root.get("classCode"), request.getClassCode()));
                }
                if (request.getTeacherId() != null) {
                    predicates.add(cb.equal(root.get("teacher").get("id"), request.getTeacherId()));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}