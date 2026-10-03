package com.spb.studentportalbackend.dto.course.response;

import com.spb.studentportalbackend.dto.course.request.AcademicTermDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateCourseClassResponse {
    Long id;
    String className;
    String classCode;
    Integer maxCapacity;
    Integer enrolledCount;
    Long teacherId;
    String teacherFullName;
    List<AcademicTermDto> targetTerms;
}
