package com.spb.studentportalbackend.repository;

import com.spb.studentportalbackend.entity.CourseClass;
import com.spb.studentportalbackend.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseClassRepository extends JpaRepository<CourseClass, Long>, JpaSpecificationExecutor<CourseClass> {

    boolean existsByClassCode(@NotBlank(message = "Class code is required") String classCode);

    Optional<CourseClass> findByClassCode(String classCode);

    @Modifying
    @Query("""
        update CourseClass c
           set c.enrolledCount = c.enrolledCount + 1,
               c.version = c.version + 1
         where c.id = :id
           and c.deletedAt is null
           and c.enrolledCount < c.maxCapacity
        """)
    int incrementEnrolledCount(@Param("id") Long id);

    @Modifying
    @Query(value = "INSERT INTO class_students (class_id, student_id) VALUES (:classId, :studentId)",
            nativeQuery = true)
    void addStudent(@Param("classId") Long classId, @Param("studentId") Long studentId);

    @Query(value = "SELECT COUNT(*) > 0 FROM class_students WHERE class_id = :classId AND student_id = :studentId",
            nativeQuery = true)
    boolean isStudentEnrolled(@Param("classId") Long classId, @Param("studentId") Long studentId);

    @Query(value = "SELECT student_id FROM class_students WHERE class_id = :classId", nativeQuery = true)
    List<Long> findEnrolledStudentIds(@Param("classId") Long classId);
}

