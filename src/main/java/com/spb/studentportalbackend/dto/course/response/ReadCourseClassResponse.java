package com.spb.studentportalbackend.dto.course.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReadCourseClassResponse {
    Long id;
    String className;
    String classCode;
    Integer maxCapacity;
    Integer enrolledCount;
    Long teacherId;
    String teacherFullName;
    boolean full;
}