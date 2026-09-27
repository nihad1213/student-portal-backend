package com.spb.studentportalbackend.dto.course.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

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
}
