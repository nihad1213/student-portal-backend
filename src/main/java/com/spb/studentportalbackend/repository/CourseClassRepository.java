package com.spb.studentportalbackend.repository;

import com.spb.studentportalbackend.entity.CourseClass;
import com.spb.studentportalbackend.entity.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseClassRepository extends JpaRepository<CourseClass, Long>, JpaSpecificationExecutor<CourseClass> {

    boolean existsByClassCode(@NotBlank(message = "Class code is required") String classCode);

    Optional<CourseClass> findByClassCode(String classCode);
}

